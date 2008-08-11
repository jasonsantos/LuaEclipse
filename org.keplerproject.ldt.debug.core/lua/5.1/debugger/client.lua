-- to use my own version of remdebug
-- but the system's own luasocket
oldrequire = require
pcall(require, 'luarocks.require')
require'socket'
oldrequire'remdebug.engine'

print('Lua Debugger Client')

opts = {...}
options = {}
files = {}

for k,v in pairs(opts) do
	if string.sub(v, 1, 2)=='--' then
		table.insert(options, string.sub(v, 3))
	else
		table.insert(files, v)
	end 
end
--print'files'
--table.foreach(files, print)
--print'options'
table.foreach(options, function(k,v)
	string.gsub(v, '(.*)=(.*)', function(key,value) options[key]=value end)
end)
--table.foreach(options, print)


remdebug.engine.config{
	host = options.host;
	port = options.port;
}

print('start', remdebug.engine.start())
print"retornou"
table.foreachi(files, function(_, file) dofile(file) end)