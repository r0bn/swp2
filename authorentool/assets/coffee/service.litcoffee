    storyTellarServices = angular.module "storyTellarServices", []

    storyTellarServices.factory 'storytellerServer', ['$http', ($http) ->
        serverUrl = "http://api.storytellar.de"
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
                $http.get("#{serverUrl}/story")
                    .success (data) ->
                        cb(data)
                    .error () ->
                        console.log "error"

            getStoryXML : (id, cb) ->
                $http.get("#{serverUrl}/story/#{id}")
                    .success (data) ->
                        cb(data)
                    .error () ->
                        console.log "error"

            createStory : (story) ->
                $http.post("#{serverUrl}/story", story)
                    .success () ->
                        console.log "created"
                    .error () ->
                        console.log "error"

        }
    ]

    storyTellarServices.factory 'xmlServices', [ () ->
        {
            isValidXML : (xml) ->
                parser = new DOMParser()

                dom = parser.parseFromString(xml, 'text/html')
                console.log dom

                if dom.getElementsByTagName('parsererror').length > 0
                    console.log dom.getElementsByTagName('parsererror')

        }
    ]


