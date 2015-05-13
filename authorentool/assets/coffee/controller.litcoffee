# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarCtrl = angular.module "storyTellarCtrl", []

    storyTellarCtrl.controller "editorCtrl", ["$scope", "$routeParams", "$http", "storytellerServer", "xmlServices", ($scope, $routeParams, $http, server, xmlService) ->

        $scope.storyId = $routeParams.story

        # Codemirror Options
        # Details: https://codemirror.net/doc/manual.html#config
        $scope.editorOptions =
            lineWrapping : true
            lineNumbers: true
            #readOnly: 'nocursor'
            mode: 'xml'
            #indentUnit : 2
            theme : "eclipse"
            foldGutter : true
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]

        $scope.testMediaFiles = (cb) ->
            wait = 0
            for file in $scope.mediaData
                wait++
                server.isMediaFileUploaded file, $scope.storyId, (mFile, status) =>
                    mFile.status = status
                    wait--
                    if wait is 0
                        cb()

        $scope.updateMediaFiles = (cb) ->
            $scope.mediaData = xmlService.getFileReferences $scope.xmlFile
            $scope.testMediaFiles () ->
                okay = true
                for f in $scope.mediaData
                    if f.status is "error"
                        okay = false
                cb okay

        $http.get("http://api.storytellar.de/story/#{$scope.storyId}")
            .success (data) ->
                $scope.xmlFile = data
                $scope.updateMediaFiles () ->
                    console.log "ini"

        # this will be initial executed and get all available story's
        $http.get("http://api.storytellar.de/story")
            .success (data) ->
                $scope.storys = data


        # this saves the current xml file
        $scope.saveXML = () ->
            test = $scope.storys[$scope.storyId]

            ret = xmlService.isValidXML $scope.xmlFile
            if ret.length > 0
                $scope.xmlError = ret
                return
            else
                $scope.xmlError = "XML valide!"

                test.xml = $scope.xmlFile

                delete test.id
                files = []
                for f in $scope.mediaData
                    if f.status is "error"
                        if f.selectedFile?
                            if f.selectedFile.name is f.name
                                files.push f.selectedFile
                            else
                                $scope.xmlError = "name must be the same as the reference in xml"
                                return
                        else
                            $scope.xmlError = "you need to add a file for every file with status error"
                            return

                server.uploadMediaFile files, test

    ]

    storyTellarCtrl.controller "homeCtrl", ["$scope", "$location", "storytellerServer", ($scope, $location, $server) ->

        # this will be initial executed and get all available story's
        $server.getStoryList (data) ->
            $scope.storys = data

        # Creates a story on the server
        $scope.createStory = () ->
            $server.createStory({})

        $scope.select = (id) ->
            $location.path("/story/#{id}")
    ]

    storyTellarCtrl.directive 'fileModel', [ () ->
        {
            restrict: 'A'
            scope :
                fileModel : '='
            link: (scope, element, attrs) ->
                
                element.bind 'change', () ->
                    scope.$apply () ->
                        scope.fileModel = element[0].files[0]
                        console.log scope.fileModel
        }
    ]


