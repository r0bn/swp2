# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarApp = angular.module "storyTellarApp", [
        'ngRoute'
        'storyTellarCtrl'
        'storyTellarServices'
        'ui.codemirror'
    ]

    storyTellarApp.config ['$routeProvider', '$locationProvider'
        ($routeProvider, $locationProvider) ->
            $routeProvider
                .when('/', {
                    templateUrl: 'home.html'
                    controller : 'homeCtrl'
                })
                .when('/story/:story', {
                    templateUrl: 'editor.html'
                    controller : 'editorCtrl'
                })
                .otherwise {
                    redirectTo: "/"
                }

            # use the HTML5 History API
            #$locationProvider.html5Mode(true)
    ]
