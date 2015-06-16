# XML Hide Viewer #

    xmlViewer = angular.module "xmlViewer", []

    xmlViewer.controller "xmlController", ["$scope", "$routeParams", "$http", "storytellerServer", "xmlServices", ($scope, $routeParams, $http, server, xmlService) ->

        $scope.storyId = $routeParams.story
        $scope.codeMirrorUpdateUI = false

        # Codemirror Options
        # Details: https://codemirror.net/doc/manual.html#config
        $scope.editorOptions =
            lineWrapping : true
            lineNumbers: true
            readOnly: 'nocursor'
            mode: 'xml'
            #indentUnit : 2
            theme : "eclipse"
            foldGutter : true
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]

        server.getStoryXML $scope.storyId, (data) ->
                $scope.xmlFile = data
                xml = $scope.xmlFile

        # this saves the current xml file
        $scope.saveXML = () ->
            ret = xmlService.isValidXML $scope.xmlFile
            if ret.length > 0
                $scope.xmlError = ret
                return
            else
                $scope.xmlError = "XML valide!"

                referencedFiles = xmlService.getFileReferences $scope.xmlFile

                for refFile in referencedFiles
                    found = false
                    console.log refFile
                    for mediaFile in $scope.mediaData
                        if "#{mediaFile.file.toLowerCase()}" is refFile.name
                            found = true
                            break
                    #if !found
                    #    $scope.xmlError = "Referenced File: #{refFile.name} not found  in media library!"
                    #    return

                console.log $scope.xmlFile
                server.updateStory $scope.storyId, $scope.xmlFile, $scope.story.final
    ]
