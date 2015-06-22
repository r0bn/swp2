
    configApp = angular.module "configApp", []

    configApp.value 'apiUrl', "http://api.storytellar.de"

    # set true if you want diasable authentication
    configApp.value 'disableAuthentication', false
