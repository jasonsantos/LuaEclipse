local lfs = require 'lfs' 
require 'logging' 
require 'luadoc' 

lp = require 'luadoc.lp'

local res = {}

function outfunc(...)
	table.foreach({...}, function(k,v) table.insert(res, v) end)
end

module (..., package.seeall)

local function include(template, env)
	lp.setoutfunc"outfunc"
	local src = getfilecontents(template)
	local prog = lp.compile (src, '@'..template)
	local _env
	if env then
		env.outfunc = outfunc
		env.table = table
		env.type = type
		env.luadoc = {}
		env.luadoc.doclet = {}
		env.luadoc.doclet.html = { symbol_link = function(...) return "" end }
		_env = getfenv (prog)
		setfenv (prog, env)
	end
	prog ()
	local sRes = table.concat(res)
	res = {}
	--print(sRes)
	return sRes
end

local function getFunctionDoc(doc, file_doc, func)
	return include("luadoc.doclet.html.function", { doc=doc, file_doc=file_doc, func=func })
end 


t = {}
--TODO: Find a way to iterate through the modules using their module names instead of their filenames

function start(doc)
	local fileOrModule = nil
	local fileOrModuleName = ''
	r = function(d)
		for k, v in pairs(d) do
			if type(v)=='table' then
				if  v.class=='function' then
					local strDoc = getFunctionDoc(doc, fileOrModule, v)
					addDocumentationEntry(fileOrModuleName, v.name, v.class, v.summary,v.description,table.concat(v.comment, '\\n'),strDoc)
				elseif v.type == 'file' or v.type == 'module' then
					fileOrModuleName =  v.name
					fileOrModule = v
				end
				r(v)
			end
		end
	end
	r(doc)
end
