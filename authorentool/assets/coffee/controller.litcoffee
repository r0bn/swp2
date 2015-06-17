# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarCtrl = angular.module "storyTellarCtrl", []

    storyTellarCtrl.controller "editorCtrl", ["$scope", "$routeParams", "$http", "storytellerServer", "xmlServices", "storytellarMedia", ($scope, $routeParams, $http, server, xmlService, media) ->

        $scope.storyId = $routeParams.story

        $scope.mediaData = media.mediaFiles
        $scope.media = media
        $scope.final = false

        $scope.$watch "media.mediaFiles", () ->
            console.log media.sumSize()
            $scope.mediaSumSize = media.sumSize()
        , true

        server.getStoryXML $scope.storyId, (response) ->
                $scope.xmlFile = response

                try
                    # XMl Synchronisation update GUI based on server file
                    console.log($scope.xmlFile)
                    startXMLSynchro($scope.xmlFile)

                catch error
                    console.log error

                media.update($scope.storyId)

        # this will be initial executed and get all available story's
        server.getStoryList (data) ->
            $scope.storys = data
            for s in $scope.storys
                if s.id is $scope.storyId
                    $scope.final = s.final
                    $scope.story = s

        # this deletes the current xml file
        $scope.deleteXML = () ->
            $("#dialog-confirm-Story").css("display","block")
            $('#dialog-confirm-Story').dialog
                  modal: true
                  buttons:
                    'Löschen': ->
                            $(this).dialog 'close'
                            $.ajax
                                  url: 'http://api.storytellar.de/story/' + $scope.storyId
                                  type: 'DELETE'
                                  success: (result) ->
                                    # Do something with the result
                                    return
                            window.location.replace("/#/home/")
                    'Abbrechen': ->
                            $(this).dialog 'close'

        # this saves the current xml file
        $scope.saveXML = () ->
            try
                # get xml file based on current gui settings
                $scope.xmlFile = startSynchro()
            catch error
                console.log error

            server.validate $scope.xmlFile, $scope.final, $scope.storyId, $scope.mediaData

        $scope.uploadMediaFile = () ->
            media.add $scope.storyId, $scope.mediaFileUpload

        $scope.deleteMediaFile = (mediaFile) ->
            media.delete mediaFile

        $scope.orderBib = (prop, reverse) ->
            media.order prop, reverse

        #jQuery Namespace Binding
        (($) ->
            ) jQuery

        
        $("#btnMap").click ->
            window.mapCaller = "btnMap"
        $("#inSize").on "input", () ->
            tmp = $(this).val().replace(/[^\d.-]/g, '')
            $(this).val(Math.abs(tmp))
        $("#inRadius").on "input", () ->
            tmp = $(this).val().replace(/[^\d.-]/g, '')
            $(this).val(Math.abs(tmp))
        initialLightMedienBox()
        initDropdownClicks()
        window.googleMap()
        
        # Counters
        window.dropdownLiCounter = 0
        window.storypointCounter = 0
        window.quizAnswerCounter = 10
        window.chooserAnswerCounter = 10
        window.interactioncounter = 10
        window.mbibCallerField = ""
        #dependencyCounter
        window.zyklusCounter = 0
        
        #everyPathArray, for checking every Path in the dependencys
        window.everyPathArray = []
        
        
        # EndstorypointArray and StartstorypointArray
        window.endStorypoints = []
        window.startStorypoints = []
        
        
        # Nodes and Edges for the dependency graph
        window.nodes = []
        window.edges = []
        window.duplicateEdges = []
        initHelpSystem()
        initScrollbar()

        window.safeButtonCounter = 0

        $("#ttaDescription").keyup ->
            aktInhalt = $(this).val()
            if aktInhalt.toLowerCase().indexOf("harlem") >= 0
                #$("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/8f7wj_RcqYk?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>');
                $("#rowFormular").children().each () ->
                    if $(this).css("display") != "none"
                        $(this).delay(21000).effect("shake", {times:100})
                return

            if aktInhalt.toLowerCase().indexOf("love") >= 0
                #$("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/6zlViU5PBPY?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>')
                return
            
            if aktInhalt.indexOf("dElEtEStOrY") >= 0
                $.ajax
                  url: 'http://api.storytellar.de/story/' + $scope.storyId
                  type: 'DELETE'
                  success: (result) ->
                    # Do something with the result
                    return
                
                window.location.replace("/#/home")
                
                    
            if aktInhalt.toLowerCase().indexOf("rekursiv") >= 0
                rekRek = aktInhalt.split("rekursiv")[0]
                $(this).val(rekRek + "rekursiv")



        #check dependencies
        $("#btnCheckStory").click ->
            window.zyklusCounter = 0
            hint = checkPlayableStory()
            $("#dialog-confirm-Playable-text").text(hint)
            $("#dialog-confirm-Playable").css("display", "block")
            $("#dialog-confirm-Playable").dialog
              modal: true
              buttons: {
                Ok: ->
                  $(this).dialog "close"
                }
            return

        $("#btnCreateNewStorypoint").click ->
            window.safeButtonCounter++
            #alert window.safeButtonCounter
            checkSafeButton()
            return

        # handeld now by angularJS
        ###
        $("#btnSaveStory").click ->
                #xml = startSynchro()
                #$scope.xmlFile = xml
                $scope.saveXML()
                if $scope.xmlError != "XML valide!"
                    $("#saveFunctionErrorText").text($scope.xmlError)
                    $("#saveFunctionError").css("display", "block")
                    $("#saveFunctionError").dialog
                      modal: true
                      buttons: {
                        Ok: -> 
                          $(this).dialog "close";
                        }
                else 
                    $("#saveFunctionSuccess").css("display", "block")
                    $("#saveFunctionSuccess").dialog
                      modal: true
                      buttons: {
                        Ok: -> 
                          $(this).dialog "close";
                        }
                return
        ###
            
        $scope.createNewStorypoint = (counter) ->
            window.storypointCounter++
            createNewStorypointX(window.storypointCounter)
            return

        
   

        $scope.tabSwitch = (activeTabID) ->
                if (activeTabID == "MedienBibTab")
                    
                     # Passive Content
                    $("#GraEditor").css("display","none")
                    $("#XML").css("display","none")
                     # Passive Tabs
                    $("#GraEditorTab").removeClass("active")
                    $("#XMLTab").removeClass("active")
                     # Active Tabs
                    $("#MedienBibTab").addClass("active")
                     # Active Content
                    $("#MedienEditor").css("display", "block")
                    return
                else if (activeTabID == "GraEditorTab") 
                     # Passive Content
                    $("#XML").css("display","none")
                    $("#MedienEditor").css("display","none")
                     # Passive Tabs
                    $("#MedienBibTab").removeClass("active")
                    $("#XMLTab").removeClass("active")
                    
                     # Active Tabs
                    $("#GraEditorTab").addClass("active")
                     # Active Content
                    $("#GraEditor").css("display", "block")
                    
                    return

        $scope.btnHelpEinklappenClick = () ->
                if $("#divHelpBox").is( ":hidden" )
                    $("#divHelpBox").show( "slow" )
                    $("#btnHelpEinklappen").find("#resize").addClass('glyphicon-resize-small')
                    $("#btnHelpEinklappen").find("#resize").removeClass('glyphicon-resize-full')
                else
                    $("#divHelpBox").slideUp("slow")
                    $("#btnHelpEinklappen").find("#resize").removeClass('glyphicon-resize-small')
                    $("#btnHelpEinklappen").find("#resize").addClass('glyphicon-resize-full')
                return

 

    ]

    storyTellarCtrl.controller "homeCtrl", ["$scope", "$location", "storytellerServer", ($scope, $location, $server) ->

        $scope.createDisabled = false

        # this will be initial executed and get all available story's
        $server.getStoryList (data) ->
            $scope.storys = data

        # Creates a story on the server
        $scope.createStory = () ->

            $scope.createDisabled = true
            $server.createStory (id)->
                $location.path "/story/#{id}"


    ]

    storyTellarCtrl.controller "loginCtrl", ["$scope", "$location", "storytellarAuthentication", ($scope, $location, $server) ->

        $scope.login = () ->
            if $server.isValid("dummy", "dummy")
                $location.path("/home")

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
        }
    ]


