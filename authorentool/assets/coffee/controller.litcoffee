# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarCtrl = angular.module "storyTellarCtrl", []

    storyTellarCtrl.controller "editorCtrl", ["$scope", "$routeParams", "$http", "storytellerServer", ($scope, $routeParams, $http, server) ->

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

        $http.get("http://api.storytellar.de/story/#{$scope.storyId}")
            .success (data) ->
                $scope.xmlFile = data

        # this will be initial executed and get all available story's
        $http.get("http://api.storytellar.de/story")
            .success (data) ->
                $scope.storys = data


        # this saves the current xml file
        $scope.saveXML = () ->
            console.log $scope.selectedFile
            test = $scope.storys[$scope.storyId]
            test.xml = $scope.xmlFile
            delete test.id
            server.uploadMediaFile [$scope.selectedFile, $scope.selectedFile2 ], test

        # This is dummy data for demmo reasons
        $scope.mediaData = [
            {
                id : 1
                name : "Cover"
                type : "image"
            },
            {
                id : 2
                name : "ReferenceBar"
                type : "image"
            },
            {
                id : 3
                name : "Introduction"
                type : "movie"
            },
            {
                id : 4
                name : "FinalScene"
                type : "movie"
            }
        ]
    ]

    storyTellarCtrl.controller "homeCtrl", ["$scope", "$location", "$http", "storytellerServer", ($scope, $location, $http, server) ->

        # this will be initial executed and get all available story's
        $http.get("http://api.storytellar.de/story")
            .success (data) ->
                $scope.storys = data

        # Creates a story on the server
        $scope.createStory = () ->
            $http.post("http://api.storytellar.de/story", $scope.storys[0])
                .success () ->
                    console.log "created"

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

    storyTellarCtrl.factory 'storytellerServer', ['$http', ($http) ->
        {
            uploadMediaFile : (files, data) ->
                $http({
                    method : 'POST'
                    url : 'http://api.storytellar.de/story'
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
        }
    ]


            

