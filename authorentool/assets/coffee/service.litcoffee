    storyTellarServices = angular.module "storyTellarServices", []

    storyTellarServices.factory 'storytellerServer', ['$http', ($http) ->
        serverUrl = "http://api.storytellar.de"
        {
            getStoryList : (cb) ->
                $http.get("#{serverUrl}/story")
                    .success (data) ->
                        for d in data
                            d.final = true
                        $http.get("#{serverUrl}/story/open")
                            .success (data2) ->
                                for d in data2
                                    d.draft = true
                                    d.final = false
                                cb(data.concat(data2))
                            .error () ->
                                console.log "error"
                    .error () ->
                        console.log "error"

            getStoryXML : (id, cb) ->
                $http.get("#{serverUrl}/story/#{id}")
                    .success (data) ->
                        cb(data)
                    .error () ->
                        console.log "error"

            createStory : (cb) ->
                $http.post("#{serverUrl}/story", { xml : "start here", working_title : "draft story" })
                    .success (data) ->
                        cb(data)
                    .error (err) ->
                        console.log err

            updateStory : (id, xml, final) ->
                $http.post("#{serverUrl}/story/#{id}", { xml : xml, final : final })
                    .success () ->
                        console.log "updated"
                    .error (err) ->
                        console.log err

        }
    ]

    storyTellarServices.factory 'storytellarMedia', [ '$http', ($http) ->
        serverUrl = "http://api.storytellar.de"
        {
            getMediaFiles : (storyId, cb) ->
                $http.get("#{serverUrl}/story/#{storyId}/media")
                    .success (data) ->
                        cb(data)
                    .error (err) ->
                        console.log err
                        cb([])

            getDownloadPath : (storyId, filename) ->
                return "#{serverUrl}/media/#{storyId}/#{filename}"

            deleteFile : (storyId, filename, cb) ->
                $http.delete("#{serverUrl}/story/#{storyId}/media/#{filename}")
                    .success (data) ->
                        cb()
                    .error (err) ->
                        console.log err

            addMediaFile : (storyId, file, cb) ->
                $http({
                    method : 'POST'
                    url : "#{serverUrl}/story/#{storyId}/media"
                    headers: {'Content-Type': undefined}
                    transformRequest : (data) ->
                        formData = new FormData()
                        formData.append "file", file
                        formData.append "_method", "PUT"
                        formData
                    data : { file : file }
                })
                .success () ->
                    cb()
                .error (err) ->
                    console.log err
        }
    ]

    storyTellarServices.factory 'storytellarAuthentication', [ '$http', 'disableAuthentication', ($http, disableAuth) ->
        serverUrl = "http://api.storytellar.de"
        isAuthenticated = disableAuth 
        {
            isValid : (user, password) ->
                isAuthenticated = true
                return true

            isAuthenticated : () ->
                return isAuthenticated

        }
    ]


    storyTellarServices.factory 'xmlServices', [ () ->
        {
            isValidXML : (xml) ->
                parser = new DOMParser()

                dom = parser.parseFromString(xml, 'text/xml')
                console.log dom

                if dom.getElementsByTagName('parsererror').length > 0
                    console.log dom.getElementsByTagName('parsererror')

                    return dom.getElementsByTagName('parsererror')[0].innerText
                else
                    return ""

            getFileReferences : (xml) ->
                xmlDoc = $.parseXML( xml.toLowerCase() )
                $xml = $( xmlDoc )

                mediaFiles = []
                
                $references = $xml.find("video").each () ->
                    mediaFiles.push {
                        name : $(this).find("href").attr("xlink:href")
                    }

                $references = $xml.find("image").each () ->
                    mediaFiles.push {
                        name : $(this).find("href").attr("xlink:href")
                    }

                return mediaFiles
                
        }
    ]


