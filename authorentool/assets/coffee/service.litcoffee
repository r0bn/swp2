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
                        $http.get("#{serverUrl}/story/open")
                            .success (data2) ->
                                for d in data2
                                    d.draft = true
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

            createStory : () ->
                $http.post("#{serverUrl}/story", { xml : "start here" })
                    .success () ->
                        console.log "created"
                    .error () ->
                        console.log "error"

            isMediaFileUploaded : (mediaFile, id, cb) ->
                $http.get("#{serverUrl}/media/#{id}/#{mediaFile.name}")
                    .success (data) ->
                        cb(mediaFile, "ok")
                    .error () ->
                        cb(mediaFile, "error")
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

            getDownloadPath : (storyId, filename) ->
                return "#{serverUrl}/media/#{storyId}/#{filename}"

            deleteFile : (storyId, filename) ->
                $http.delete("#{serverUrl}/story/#{storyId}/media")
                    .success (data) ->
                        console.log data
                    .error (err) ->
                        console.log err

            addMediaFile : (storyId, file) ->
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
                    console.log "success"
                .error (err) ->
                    console.log err
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


