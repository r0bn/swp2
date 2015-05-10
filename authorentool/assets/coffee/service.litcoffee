    mainApp.factory 'storytellerServer', ['$http', ($http) ->
        serverPath = "http://api.storytellar.de"
        {
            uploadMediaFile : (files, data) ->
                $http({
                    method : 'POST'
                    url : 'http://api.dev.la/createstory'
                    headers: {'Content-Type': undefined}
                    transformRequest : (data) ->
                        formData = new FormData()

                        for key, value of data.model
                            formData.append key, value

                        for file in data.files
                            formData.append "media[]", file

                        formData
                    data : { files : files, model : data }
                })
                .success () ->
                    console.log "success"
                .error () ->
                    console.log "error"

            getStoryList : (cb) ->
                $http.get("#{basePath}/story")
                    .success (data) ->
                        cb(data)
                    .error () ->
                        console.log "error"



        }
    ]



