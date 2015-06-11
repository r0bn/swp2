# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarApp = angular.module "storyTellarApp", [
        'ngRoute'
        'storyTellarCtrl'
        'storyTellarServices'
        'configApp'
        'ui.codemirror'
    ]

    storyTellarApp.config ['$routeProvider', '$locationProvider'
        ($routeProvider, $locationProvider) ->
            $routeProvider
                .when('/', {
                    templateUrl: 'login.html'
                    controller : 'loginCtrl'
                })
                .when('/home/', {
                    templateUrl: 'home.html'
                    controller : 'homeCtrl'
                    auth : true
                })
                .when('/story/:story', {
                    templateUrl: 'editor.html'
                    controller : 'editorCtrl'
                    auth : true
                })
                .otherwise {
                    redirectTo: "/"
                }

            # use the HTML5 History API
            #$locationProvider.html5Mode(true)
    ]

    # check authentication
    storyTellarApp.run ($location, $rootScope, $route, storytellarAuthentication) ->
        $rootScope.$on '$locationChangeStart', (evt, next, current) ->
            nextPath = $location.path()
            nextRoute = $route.routes[nextPath]

            if nextRoute && nextRoute.auth && !storytellarAuthentication.isAuthenticated()
                $location.path("/")

            if nextPath is "/" && storytellarAuthentication.isAuthenticated()
                $location.path("/home")

