CLIENT_LAUNCH_MODE = true

-- to use my own version of remdebug
-- but the system's own luasocket
oldrequire = require
--pcall(require, 'luarocks.require')
require'socket'

print('LuaEclipse RemDebug Client')

local options = {}
local file

local killIndices = {}
for i,v in ipairs(arg) do
	if string.sub(v, 1, 2)=='--' then
		table.insert(options, string.sub(v, 3))
                table.insert(killIndices, i)
	else
		file = file or v
	end 
end

for i = #killIndices,1,-1 do
    table.remove(arg, i)
end
arg[0] = arg[1]
table.remove(arg, 1)

table.foreach(options, function(k,v)
	string.gsub(v, '(.*)=(.*)', function(key,value) options[key]=value end)
end)

local path = package.path 
package.path=";;"..tostring(options.prefix).."/?.lua"
oldrequire'remdebug.engine'
package.path=path

remdebug.engine.config {
	host = options.host;
	port = options.port;
}

print('start', remdebug.engine.start())

local path = string.gsub(file, "(.-)/[^/]-$", "%1") or '.'
package.path = package.path .. ";" .. path .. "/?.lua"
remdebug.engine.launch(file)
