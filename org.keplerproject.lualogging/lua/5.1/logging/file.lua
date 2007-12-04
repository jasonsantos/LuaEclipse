-------------------------------------------------------------------------------
-- Saves logging information in a file
--
-- @author Thiago Costa Ponte (thiago@ideais.com.br)
--
-- @copyright 2004-2007 Kepler Project
--
-- @release $Id$
-------------------------------------------------------------------------------

require"logging"

function logging.file(filename, datePattern, logPattern)

    if type(filename) ~= "string" then
        filename = "lualogging.log"
    end
    filename = string.format(filename, os.date(datePattern))
    local f = io.open(filename, "a")
    if not f then
       return nil, string.format("file `%s' could not be opened for writing", filename)
    end
    f:setvbuf ("line")

    return logging.new( function(self, level, message)
                            local s = logging.prepareLogMsg(logPattern, os.date(), level, message)
                            f:write(s)
                            return true
                        end
                      )
end
