    storyTellarServices = angular.module "storyTellarServices", []

    storyTellarServices.factory 'storytellerServer', ($http, xmlServices, apiUrl) ->
        m = {
            getStoryList : (cb) ->
                $http.get("#{apiUrl}/story")
                    .success (data) ->
                        for d in data
                            d.final = true
                        $http.get("#{apiUrl}/story/open")
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
                $http.get("#{apiUrl}/story/#{id}")
                    .success (data) ->
                        cb(data)
                    .error () ->
                        console.log "error"

            createStory : (cb) ->
                $http.post("#{apiUrl}/story", { xml : "start here", working_title : "draft story" })
                    .success (data) ->
                        cb(data)
                    .error (err) ->
                        console.log err

            updateStory : (id, xml, final) ->
                $http.post("#{apiUrl}/story/#{id}", { xml : xml, final : final })
                    .success () ->
                        
                        $("#saveFunctionSuccess").css("display", "block")
                        $("#saveFunctionSuccess").dialog
                          modal: true
                          buttons: {
                            Ok: -> 
                              $(this).dialog "close";
                            }
                        
                        
                        console.log "updated"
                    .error (err) ->
                        tempErrValue = "Story konnte nicht an den Server gesendet werden!\n#{err}"
                        
                        $("#saveFunctionErrorText").text(tempErrValue)
                        $("#saveFunctionError").css("display", "block")
                        $("#saveFunctionError").dialog
                          modal: true
                          buttons: {
                            Ok: -> 
                              $(this).dialog "close";
                            }
                        
                        console.log err

            # this saves the current xml file
            validate : (xml, final, storyId, mediaFiles) ->
                # only do checks if the save should be final
                if final

                    ret = xmlServices.isValidXML xml
                    if ret.length > 0
                        tempErrValue =  "xml nicht formgerecht:\n#{ret}"
                        
                        $("#saveFunctionErrorText").text(tempErrValue)
                        $("#saveFunctionError").css("display", "block")
                        $("#saveFunctionError").dialog
                          modal: true
                          buttons: {
                            Ok: -> 
                              $(this).dialog "close";
                            }
                        
                        return
                    else
                        referencedFiles = xmlServices.getFileReferences xml
                        console.log "Referenced Files:"
                        console.log referencedFiles
                        console.log xml
                        for refFile in referencedFiles
                            found = false
                            for mediaFile in mediaFiles
                                if "#{mediaFile.file.toLowerCase()}" is refFile.name
                                    found = true
                                    break
                            if !found
                                tempErrValue = "Referenced File: #{refFile.name} not found  in media library! - Cancel"
                                
                                $("#saveFunctionErrorText").text(tempErrValue)
                                $("#saveFunctionError").css("display", "block")
                                $("#saveFunctionError").dialog
                                  modal: true
                                  buttons: {
                                    Ok: -> 
                                      $(this).dialog "close";
                                    }
                                
                                return

                m.updateStory storyId, xml, final


        }

    storyTellarServices.factory 'storytellarMedia', ($http, apiUrl, $filter) ->
        m = {
            mediaFiles : []

            isUploading : false
            isDeleting : false
            orderBy : $filter('orderBy')

            update : (storyId) ->
                $http.get("#{apiUrl}/story/#{storyId}/media")
                    .success (data) ->
                        angular.copy data, m.mediaFiles
                        for mF in m.mediaFiles
                            mF.storyId = storyId
                        m.order('file', false)
                        m.isUploading = false
                    .error (err) ->
                        angular.copy [], m.mediaFiles
                        console.log err

            order : (predicate, reverse) ->
                angular.copy m.orderBy(m.mediaFiles, predicate, reverse), m.mediaFiles

            getDownloadPath : (mediaFile) ->
                return "#{apiUrl}/media/#{mediaFile.storyId}/#{mediaFile.filename}"

            delete : (mediaFile) ->
                m.isDeleting = true
                $http.delete("#{apiUrl}/story/#{mediaFile.storyId}/media/#{mediaFile.file}")
                    .success (data) ->
                        m.mediaFiles.splice m.mediaFiles.indexOf(mediaFile), 1
                    .error (err) ->
                        alert err
                    .finally () ->
                        m.isDeleting = false

            sumSize : () ->
                sum = 0
                for mF in m.mediaFiles
                    sum += mF.size
                return sum

            add : (storyId, file) ->
                m.isUploading = true
                $http({
                    method : 'POST'
                    url : "#{apiUrl}/story/#{storyId}/media"
                    headers: {'Content-Type': undefined}
                    transformRequest : (data) ->
                        formData = new FormData()
                        formData.append "file", file
                        formData.append "_method", "PUT"
                        formData
                    data : { file : file }
                })
                .success () ->
                    m.update(storyId)
                .error (err) ->
                    alert err
        }

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


