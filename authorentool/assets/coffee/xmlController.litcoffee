# XML Hide Viewer #

    xmlViewer = angular.module "xmlViewer", []

    xmlViewer.controller "xmlController", ($scope, $routeParams, $http, storytellerServer, xmlServices, storytellarMedia) ->

        media = storytellarMedia
        server = storytellerServer
        xmlService = xmlServices

        $scope.storyId = $routeParams.story
        $scope.codeMirrorUpdateUI = false

        $scope.mediaData = media.mediaFiles

        $scope.final = false

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
                media.update($scope.storyId)


        # this saves the current xml file
        $scope.saveXML = () ->
            server.validate $scope.xmlFile, $scope.final, $scope.storyId, $scope.mediaData
