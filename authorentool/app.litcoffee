        express         = require "express"
        app             = express()

        app.set 'view engine', 'jade'
        app.set 'views', 'server/views'

        app.use '/bower', express.static('bower_components')
        app.use '/static', express.static('static')

        app.get '/', (req, res) ->
            res.render "main.jade"

        server = app.listen 4000, () ->

            host = server.address().address
            port = server.address().port

            console.log "Example app listening at http://#{ host }:#{ port }"
