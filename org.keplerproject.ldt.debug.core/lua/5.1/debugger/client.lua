-- to use my own version of remdebug
-- but the system's own luasocket
oldrequire = require
pcall(require, 'luarocks.require')
require'socket'

print('LuaEclipse RemDebug Client')

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

table.foreach(options, function(k,v)
	string.gsub(v, '(.*)=(.*)', function(key,value) options[key]=value end)
end)

package.path=";;"..tostring(options.prefix).."/?.lua"
oldrequire'remdebug.engine'


remdebug.engine.config{
	host = options.host;
	port = options.port;
}

print('start', remdebug.engine.start())

table.foreachi(files, function(_, file) dofile(file) end)