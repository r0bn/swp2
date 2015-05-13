# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarCtrl = angular.module "storyTellarCtrl", []

    storyTellarCtrl.controller "editorCtrl", ["$scope", "$routeParams", "$http", "storytellerServer", "xmlServices", ($scope, $routeParams, $http, server, xmlService) ->

        $scope.selectedFile = ""
        $scope.selectedFile2 = ""
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

        $scope.testMediaFiles = () ->
            for file in $scope.mediaData
                server.isMediaFileUploaded file, $scope.storyId, (mFile, status) =>
                    mFile.status = status
                    console.log status

        $http.get("http://api.storytellar.de/story/#{$scope.storyId}")
            .success (data) ->
                $scope.xmlFile = data
                $scope.mediaData = xmlService.getFileReferences $scope.xmlFile
                $scope.testMediaFiles()

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

                xmlService.getFileReferences $scope.xmlFile

                test.xml = $scope.xmlFile

                delete test.id
                #server.uploadMediaFile [$scope.selectedFile, $scope.selectedFile2 ], test

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


