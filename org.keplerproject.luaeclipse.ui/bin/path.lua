path = package.path
parse = true
while parse do
	last = string.find(path, ";")
	if last == nil then
		parse = false
		last = string.find(path, '?')
		if last == nil then
			print( path )
		else
			print( string.sub(path, 1, last - 1) )
		end
	else
		print( string.sub(path, 1, string.find(path, '?') - 1) )
		path = string.sub(path, last + 1);
	end
end

