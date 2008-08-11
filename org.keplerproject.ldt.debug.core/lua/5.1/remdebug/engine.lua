--
-- RemDebug 2.0 Beta
-- Copyright Kepler Project 2005 (http://www.keplerproject.org/remdebug)
--

pcall(require, "luarocks.require")

local socket = require"socket"
local lfs = require"lfs"
local debug = require"debug"

io.open('/tmp/debug.log', 'w'):close()

print = function(...) local f = io.open('/tmp/debug.log', 'a+') table.foreachi({...}, function(k,v) f:write(tostring(v)) f:write'\t' end) f:write'\n' f:close() end

module("remdebug.engine", package.seeall)

_COPYRIGHT = "2006 - Kepler Project"
_DESCRIPTION = "Remote Debugger for the Lua programming language"
_VERSION = "2.0.0beta"


local coro_debugger
local events = { BREAK = 1, WATCH = 2 }
local breakpoints = {}
local callstack= {}
local localsstack= {}
local watches = {}
local step_into = false
local step_over = false
local step_level = 0
local stack_level = 0

local controller_host = "localhost"
local controller_port = 8171
local events_port = 60000

-- - Breakpoints

local function set_breakpoint(file, line)
  breakpoints[file] = breakpoints[file] or {}
  breakpoints[file][line] = true  
end

local function remove_breakpoint(file, line)
  if breakpoints[file] then
    breakpoints[file][line] = nil
  end
end

local function has_breakpoint(file, line)
  return breakpoints[file] and breakpoints[file][line]
end

-- - Variables

local function restore_vars(vars)
  if type(vars) ~= 'table' then return end
  local func = debug.getinfo(3, "f").func
  local i = 1
  local written_vars = {}
  while true do
    local name = debug.getlocal(3, i)
    if not name then break end
    debug.setlocal(3, i, vars[name])
    written_vars[name] = true
    i = i + 1
  end
  i = 1
  while true do
    local name = debug.getupvalue(func, i)
    if not name then break end
    if not written_vars[name] then
      debug.setupvalue(func, i, vars[name])
      written_vars[name] = true
    end
    i = i + 1
  end
end

local function capture_vars()
  local vars = {}
  local func = debug.getinfo(3, "f").func
  local i = 1
  while true do
    local name, value = debug.getupvalue(func, i)
    if not name then break end
    vars[name] = value
    i = i + 1
  end
  i = 1
  while true do
    local name, value = debug.getlocal(3, i)
    if not name then break end
    vars[name] = value
    i = i + 1
  end
  setmetatable(vars, { __index = getfenv(func), __newindex = getfenv(func) })
  return vars
end

-- - Dir Utils

local function break_dir(path) 
  local paths = {}
  path = string.gsub(path, "\\", "/")
  for w in string.gfind(path, "[^\/]+") do
    table.insert(paths, w)
  end
  return paths
end

local function merge_paths(path1, path2)
print('merge_paths', path1, path2)
  local paths1 = break_dir(path1)
  local paths2 = break_dir(path2)
  for i, path in ipairs(paths2) do
    if path == ".." then
      table.remove(paths1, table.getn(paths1))
    elseif path ~= "." then
      table.insert(paths1, path)
    end
  end
  return table.concat(paths1, "/")
end

-- - Hook Engine

local function getFileName(f)
 if string.find(f, "@") == 1 then
      return string.sub(f, 2)
 else
      return nil
 end
end



local function fill_callstack()
	callstack={}
	localsstack={}
	local level = 3 -- the function level to debug
	local level_info = debug.getinfo(level)
	while level_info do
		i = 1
		key, value = debug.getlocal(level, i)
		local locals = {}
		while key do
			locals[key] = value
			i = i + 1
			key, value = debug.getlocal(level, i)
		end
		print('stack level', level, level_info.name, level_info.namewhat, level_info.source, level_info.short_src, level_info.currentline )
		table.insert(callstack, 1, level_info)
		table.insert(localsstack, 1, locals)
		level = level +1
		level_info = debug.getinfo(level)
	end
end

local function debug_hook(event, line)
  if event == "call" then
    stack_level = stack_level + 1
  elseif event == "return" then
    stack_level = stack_level - 1
  else
    local file = getFileName(debug.getinfo(2, "S").source)
    if not file  then
      print('--unknown file --', line)
    end
    -- TODO: eliminate dependency on lfs
    --file = merge_paths(lfs.currentdir(), file)
    
    -- Watches
    local vars = capture_vars()
    table.foreach(watches, function (index, value)
      setfenv(value, vars)
      local status, res = pcall(value)
      if status and res then
      print'debugging..'
      print('-------------', status, res)
       -- TODO: send watch events
        coroutine.resume(coro_debugger, events.WATCH, vars, file, line, index)
	restore_vars(vars)
      end
    end)
      
    if step_into or (step_over and stack_level <= step_level) or has_breakpoint(file, line) then
      step_into = false
      step_over = false
      print(">> resuming to coroutine..", file, line)
    
      -- stack
      fill_callstack()
    
      coroutine.resume(coro_debugger, events.BREAK, vars, file, line)
      restore_vars(vars)
    end
  end
end

-- -- Operation Engine

local commands = {}

local SUCCESS = "200 OK\n"
local BAD_REQUEST = "400 Bad Request\n"
local EXPRESSION_ERROR_ = "401 Error in Expression "
local STACK_DUMP_ = "101 Stack "
local VARIABLE_DUMP_ = "102 Variable "
local BREAK_PAUSE_ = "202 Paused "
local WATCH_PAUSE_ = "203 Paused "
local EXECUTION_ERROR_ = "401 Error in Execution "


local eventSink

function commands.createEventSocket(server, port)
	eventSink = socket.connect(controller_host, port)
	if eventSink then
		return SUCCESS
	else
		return EXECUTION_ERROR_
	end
end

--- implements SETB command
function commands.setBreakpoint(server, filename, lineNumber )
	if filename and lineNumber then
		filename = string.gsub(filename, "%%20", " ")
	print('breakpoint filename:', filename)
	    set_breakpoint(filename, tonumber(lineNumber))
	    
	    eventSink:send(SUCCESS) -- TODO: create specific event?
	    
	    return SUCCESS 
	else
	print'no parameters'
	    return BAD_REQUEST
	end
end

--- implements DELB command
function commands.removeBreakpoint(server, filename, lineNumber )
	if filename and lineNumber then
		filename = string.gsub(filename, "%%20", " ")
	    remove_breakpoint(filename, tonumber(lineNumber))
		
	    eventSink:send(SUCCESS) -- TODO: create specific event?
	    
	    return SUCCESS 
	else
	    return BAD_REQUEST
	end
end

function commands.execute(server, status, chunk)
	if chunk then 
        local func = loadstring(chunk)
        local status, res
        if func then
        	setfenv(func, eval_env)
        	status, res = xpcall(func, debug.traceback)
        end
        res = tostring(res)
        if status then
        	local s = SUCCESS .. " " .. string.len(res) .. "\n" 
		
		eventSink:send(s) 
		
        	return s .. res
        else
        	local s = EXPRESSION_ERROR_ .. string.len(res) .. "\n"
        	return s .. res
        end
	else
    	return BAD_REQUEST
	end
end

function commands.setWatch(server, status, exp)
	if exp then 
		local func = loadstring("return(" .. exp .. ")")
		local newidx = table.getn(watches) + 1
		watches[newidx] = func
		table.setn(watches, newidx)
		
		local s = SUCCESS .. " " .. newidx .. "\n" 
		
		eventSink:send(s) 
		
		return s
	else
		return BAD_REQUEST
	end  
end

function commands.deleteWatch(server, status, index)
	index = tonumber(index)
	if index then
		watches[index] = nil
		eventSink:send(SUCCESS) 
		return SUCCESS 
	else
		return BAD_REQUEST
	end
end

function commands.run(server)
print'run success'
--	server:send(SUCCESS)
	eventSink:send(SUCCESS) 
    
	local ev, vars, file, line, idx_watch = coroutine.yield()
    
	eval_env = vars
    
	local s
    
	if ev == events.BREAK then
		s = BREAK_PAUSE_ .. file .. " " .. line .. "\n"
	elseif ev == events.WATCH then
		s = WATCH_PAUSE_ .. file .. " " .. line .. " " .. idx_watch .. "\n"
	else
		s = EXECUTION_ERROR_ .. string.len(file) .. "\n" .. file
	end
	
	eventSink:send(s) 
	
	return s
end

function commands.step(server)
print'step success' print(SUCCESS)
	--server:send(SUCCESS)
	eventSink:send(SUCCESS) 
print'step success sent'
	step_into = true
	print'\n\n\nYielding process\n';
	local ev, vars, file, line, idx_watch = coroutine.yield()
	print'\n\n\nReturned to here!\n';
	
	eval_env = vars
	
	local s
    
	if ev == events.BREAK then
		s = BREAK_PAUSE_ .. file .. " " .. line .. "\n"
	elseif ev == events.WATCH then
		s = WATCH_PAUSE_ .. file .. " " .. line .. " " .. idx_watch .. "\n"
	else
		s = EXECUTION_ERROR_ .. string.len(file) .. "\n" .. file
	end
	
	eventSink:send(s) 
	
	return s
end

function commands.stepOver(server)
print'stepover success'
	eventSink:send(SUCCESS) 
	--server:send(SUCCESS)
print'step success sent'
	
	step_over = true
	step_level = stack_level
	local ev, vars, file, line, idx_watch = coroutine.yield()
	
	eval_env = vars
	
	local s
    
	if ev == events.BREAK then
		s = BREAK_PAUSE_ .. file .. " " .. line .. "\n"
	elseif ev == events.WATCH then
		s = WATCH_PAUSE_ .. file .. " " .. line .. " " .. idx_watch .. "\n"
	else
		s = EXECUTION_ERROR_ .. string.len(file) .. "\n" .. file
	end
	
	eventSink:send(s) 
	
	return s
end

function commands.variable(server, stackLevel, variableName)
print'commands.stack success'
	local locals = localsstack[tonumber(stackLevel)] or {}
table.foreach(localsstack, print)
print(locals, #locals, variableName)
table.foreach(locals, print)
print(locals[variableName])
	local value = locals[variableName]
	local s = tostring(value) 
xpcall(function()

	if type(value)=='table' then
		s = s .. "#"
		for key, value in pairs(value) do
			s = s .. tostring(key) .. '=' .. tostring(value) .. "|"
		end
	end
	s = s .. '\n'
	eventSink:send(VARIABLE_DUMP_ .. stackLevel .. " " .. variableName .. s) 
end, print)

	return s

end

function commands.stack(server)
print'commands.stack success'
	local stack = {}
	local i=stack_level
	print('beginning at ' .. i)
	local __ = function(s) return string.gsub(tostring(s or ''), '([|#])', '\\%1') end -- escaped control characters
	local _ = function(s) return __(s) .. '|' end 
	
	local join = function(t) 
		local tt = {}
		for key, value in pairs(t) do 
			table.insert(tt, __(key) .. "=" .. type(value) )
		end 
		return table.concat(tt, '|') 
	end
	
	for i, aa in pairs(callstack) do
		if aa then
			local locals = localsstack[i]
			table.insert(stack, _(i) .. _(aa.name) .. _(aa.namewhat) .. _(getFileName(aa.source)) .. _(aa.short_src) .. _( aa.currentline ) .. join(locals))
		end
	end
	print'stopped...'
	local s = STACK_DUMP_ .. table.concat(stack, '#') .. '\n'
	
	eventSink:send(s) 
	
	return s
end


function commands.exit(server)
print'commands.exit success'
	if eventSink then 
		eventSink:close() 
	end
	if server then 
		server:close() 
	end
	os.exit(1)
end




-- -- Debugger Loop

local function debugger_loop(server)
	local command
	local eval_env = {}
  
	-- command x operations table.. allows alternate syntaxes to commands
	local operations = {
	  	SUBSCRIBE={
	  		operation = 'createEventSocket',
	  		paramsMask = '(%d+)$'
	  	},
	  	SETB={
	  		operation = 'setBreakpoint',
	  		paramsMask = '([%w%p]+)%s+(%d+)$'
	  	},
	  	DELB={
	  		operation = 'removeBreakpoint', 
	  		paramsMask = '([%w%p]+)%s+(%d+)$'
	  	},
	  	EXEC={
	  		operation = 'execute', 
	  		paramsMask = '.*'
	  	},
	  	SETW={
	  		operation = 'setWatch', 
	  		paramsMask = '.*'
	  	},
	  	DELW={
	  		operation = 'deleteWatch', 
	  		paramsMask = '(%d+)'
	  	},
	  	RUN ={
	  		operation = 'run', 
	  		paramsMask = '.*'
	  	},
	  	RESUME ={
	  		operation = 'run', 
	  		paramsMask = '.*'
	  	},
	  	EXIT ={
	  		operation = 'exit', 
	  		paramsMask = '.*'
	  	},
	  	STACK ={
	  		operation = 'stack', 
	  		paramsMask = '.*'
	  	},
	  	EXAMINE ={
	  		operation = 'variable', 
	  		paramsMask = '(%d+) (.+)'
	  	},
	  	STEP={
	  		operation = 'step', 
	  		paramsMask = '.*'
	  	},
	  	OVER={
	  		operation = 'stepOver', 
	  		paramsMask = '.*'
	  	},
	}
  
	while true do
print"I'm about to receive from server"
	    local line, status = server:receive()
print('received', "'" .. tostring(line) .. "'", ">" .. tostring(status).. "<")
	    local result  = BAD_REQUEST

	for command, params in string.gmatch(line, "([A-Z]+)%s*(.*)") do
print('found:', command, params)
		print('executing command:',command, params)
		local operation = operations[command] and operations[command].operation
		print('executing...', tostring(command), tostring(operation))
		if operation then
			print('about to run', tostring(operation), tostring(commands[operation]), tostring(params),  operations[command].paramsMask)
			local outparams = {}
			string.gsub(params, operations[command].paramsMask, function(...)
				outparams = {...}
			end)
			print'parameters'
			table.foreach(outparams, print)
			result = commands[operation](server, unpack(outparams))
			
			print('r',result)
		end    	
        end
print('R',result)
    	server:send(result)
	end  
end

-- -- Creating the Coroutine Debugger -- -- 

coro_debugger = coroutine.create(debugger_loop)


-- -- Public Interface -- -- 

--
-- remdebug.engine.config(tab)
-- Configures the engine
--
function config(tab)
  if tab.host then
    controller_host = tab.host
  end
  if tab.port then
    controller_port = tab.port
  end
end

--
-- remdebug.engine.start()
-- Tries to start the debug session by connecting with a controller
--
function start()
  pcall(require, "remdebug.config")
  print('Connecting...', controller_host, controller_port)
  local server = socket.connect(controller_host, controller_port)
  if server then
    print'Success!'
    
    _TRACEBACK = function (message) 
      local err = debug.traceback(message)
      server:send(EXECUTION_ERROR_ .. string.len(err) .. "\n")
      server:send(err)
      server:close()
      return err
    end
    debug.sethook(debug_hook, "lcr")
    print'waiting..'
    return coroutine.resume(coro_debugger, server)
  else
    error('Could not connect to server: ' .. (msg or ''))
  end
end

