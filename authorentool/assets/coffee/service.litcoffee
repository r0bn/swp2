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


