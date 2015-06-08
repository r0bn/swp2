# Client Logic #

The following code is a angularJS (https://angularjs.org/) Application.

    storyTellarCtrl = angular.module "storyTellarCtrl", []

    storyTellarCtrl.controller "editorCtrl", ["$scope", "$routeParams", "$http", "storytellerServer", "xmlServices", "storytellarMedia", "$filter", ($scope, $routeParams, $http, server, xmlService, media, $filter) ->

        $scope.storyId = $routeParams.story
        $scope.codeMirrorUpdateUI = false
        orderBy = $filter('orderBy')

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

        server.getStoryXML $scope.storyId, (data) ->
                $scope.xmlFile = data
                $scope.updateMedia()

        $scope.updateMedia = () ->
            media.getMediaFiles $scope.storyId, (mediaFiles) ->
                $scope.mediaData = mediaFiles
                $scope.orderBib('file', false)

        $scope.orderBib = (predicate, reverse) ->
            $scope.mediaData = orderBy($scope.mediaData, predicate, reverse)


        # this will be initial executed and get all available story's
        server.getStoryList (data) ->
            $scope.storys = data
            for s in $scope.storys
                if s.id is $scope.storyId
                    console.log s
                    $scope.story = s


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
                    if !found
                        $scope.xmlError = "Referenced File: #{refFile.name} not found  in media library!"
                        return

                console.log $scope.xmlFile
                server.updateStory $scope.storyId, $scope.xmlFile, $scope.story.final

        $scope.uploadMediaFile = () ->
            media.addMediaFile $scope.storyId, $scope.mediaFileUpload, () ->
                $scope.updateMedia()

        $scope.deleteMediaFile = (filename) ->
            media.deleteFile $scope.storyId, filename, () ->
                $scope.updateMedia()

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
        lightMedienBox()
        initDropdownClicks()
        window.googleMap()
        
        # Counters
        window.dropdownLiCounter = 0
        window.storypointCounter = 0
        window.quizAnswerCounter = 10
        window.chooserAnswerCounter = 10
        window.interactioncounter = 10
        
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
                $("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/8f7wj_RcqYk?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>');
                $("#rowFormular").children().each () ->
                    if $(this).css("display") != "none"
                        $(this).delay(21000).effect("shake", {times:100})
                return

            if aktInhalt.toLowerCase().indexOf("love") >= 0
                $("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/6zlViU5PBPY?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>')
                return


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

        $("#btnSaveStory").click ->
                xml = startSynchro()
                $scope.xmlFile = xml
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

            
        $scope.createNewStorypoint = (counter) ->
        
                window.storypointCounter = counter
                copyForm = document.getElementById("fhlNeuerStorypoint")
                stuff = copyForm.cloneNode(true)
                counter = 1 if counter == undefined
                stuff.id="fhlNeuerStorypoint_" + counter
                stuff.style.display="block"
                document.getElementById("fhlStorypoints").appendChild(stuff)
                # Setze die IDs neu
                setIDs($("#" + stuff.id), counter)
                # Labels neu setzen
                $("#lblInputStorypoint_" + counter).attr("for","inStorypoint_" + counter)
                # Set Storyname Placeholder 
                actPlaceholder = $("#inStorypoint_"+counter).attr("placeholder") + " "
                $("#inStorypoint_"+counter).attr("placeholder", actPlaceholder + counter)
                
                # FeatureName Trigger
                $("#inStorypoint_"+counter).keyup ->
                    inIDSetter($("#inStorypoint_"+counter), $("#lgdNeuerStorypointFieldset_"+counter), "Storypoint: ", "Neuer Storypoint")
                    i = 0;
                    while i < window.nodes.length
                        if window.nodes[i].id == counter
                            if $("#inStorypoint_"+counter).val() != ""
                                window.nodes[i].label = $("#inStorypoint_"+counter).val()
                            else
                                window.nodes[i].label = "Storypoint: " + counter
                        i++ 
                    container = document.getElementById('divDependencyBox')
                    data = {
                        nodes: window.nodes,
                        edges: window.edges
                        }
                    network = new vis.Network(container, data, {});
                    
                    

                btnSwitchDown("#btnSwitchDown_" + counter, "#" + stuff.id)
                btnSwitchUp("#btnSwitchUp_" + counter, "#" + stuff.id)
                
                
                
                $("#btnStorypointMap_" + counter).attr("gpsField", $("#btnStorypointMap_" + counter).attr("gpsField") + "_" + counter)
                # Click Event für btnStorypointMap                 
                $("#btnStorypointMap_" + counter).click ->
                    window.mapCaller = "btnStorypointMap_" + counter


                # Click Event für btnNeuesStorypointDelete
                $("#btnNeuesStorypointDelete_" + counter).click ->
                    $("#dialog-confirm-Storypoint").css("display","block")
                    $('#dialog-confirm-Storypoint').dialog
                      modal: true
                      buttons:
                        'Löschen': ->
                                $(this).dialog 'close'
                                window.safeButtonCounter--
                                checkSafeButton()
                                rowCounter = $("#btnCreateReferences_" + counter).attr("rowCounter")
                                if typeof rowCounter != 'undefined'
                                    row = 1
                                    while row <= rowCounter
                                        if typeof $("#btnStorypointRefDelete_"+ counter + "_" + row + "_1").attr("id") != 'undefined'
                                            storypoint = $("#btnSetStorypointReferences_"+counter + "_" + row + "_1").attr("selectedOwner")
                                            if typeof storypoint != "undefined"
                                                storypoint = storypoint.split("_")
                                                columnCounter = 1
                                                previousStorypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +row + "_" + columnCounter, "fhlNeuerStorypoint" )
                                                previousStorypoint = previousStorypoint.split("_")
                                                RemoveParticularEdge(storypoint[1],previousStorypoint[1])
                                                i = 1
                                                i++
                                                while i < 4
                                                   nextStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +row + "_" + i).attr("selectedOwner")
                                                   if typeof nextStorypoint != "undefined" && typeof storypoint != "undefined"
                                                       nextStorypoint = nextStorypoint.split("_")
                                                       RemoveParticularEdge(nextStorypoint[1], storypoint[1])
                                                   storypoint = nextStorypoint
                                                   i++
                                                $("#fgpMultipleStorypointRefs_" + counter + "_" + row).remove()
                                                $("#fgpMultipleItemRefs_" + counter + "_" + row).remove() 

                                        row++
                                AddoDeleteNewNodes("", $("#fhlNeuerStorypoint_" + counter).attr("nodeOwner"), counter)
                                $("#fhlNeuerStorypoint_" + counter).toggle "drop", 200, () ->
                                    $("#fhlNeuerStorypoint_" + counter).remove()
                                return
                        'Abbrechen': ->
                                $(this).dialog 'close'
                                return
                        
        
                # Click Event für btnStorypointEinklappen
                btnEinklappen("#btnStorypointEinklappen_" + counter, "#fstNeuesStorypointContent_" + counter)


                # Click Event für btnSetStorypointReferences
                $("#btnSetStorypointReferences_"+ counter).click ->
                    createDropdownStorypointRef(counter, "#btnSetStorypointReferences_" + counter, "#" + stuff.id)

                # Click Event für btnCreateReferences
                $("#btnCreateReferences_" + counter).click ->
                    if typeof $("#btnCreateReferences_" + counter).attr("rowCounter") == 'undefined'
                        $("#btnCreateReferences_" + counter).attr("rowCounter", "1")
                    else
                        i = $("#btnCreateReferences_" + counter).attr("rowCounter")
                        i++
                        $("#btnCreateReferences_" + counter).attr("rowCounter", i)
                    createStorypointRef(counter, $("#btnCreateReferences_" + counter).attr("rowCounter"), "#" + stuff.id)
                # Click Event für btnCreateInteraction
                $("#btnCreateInteraction_" + counter).click ->
                    if ($("#ddnInteractions_" + counter).val() == "Item")
                        createItem(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "Quiz")
                        createQuiz(counter)
                    else if ($("#ddnInteractions_" + counter).val() == "Chooser")
                        createChooser(counter)
                # Click Events für ddnInteraction
                initDdnInteraction(counter)
                # if counter > 6
                    # $("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/lTcRfJyEKkM?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>');
                    
                # Neues Feature Button machen.
                button = document.getElementById("fgpStorypoint");
                button.parentNode.removeChild(button);
                document.getElementById("fhlStorypoints").appendChild(button)
                button.scrollIntoView(true)
                # Für jeden Storypoint gibt es ein Attribut nodeOwner, mit der ID: Storypoint_ (counter)
                $("#fhlNeuerStorypoint_" + counter).attr("nodeOwner", "Storypoint_" + counter)
                # Create new Node after creating a new Storypoint
                AddoDeleteNewNodes( "Storypoint: " +counter ,"", counter )
                helpRek($("#" + stuff.id))

                #ClickEvent für inEndOfStory_Checkbox
                $("#inEndOfStory_"+counter).click ->
                    if $("#inEndOfStory_"+counter).is(" :checked ")
                        
                            $("#dialog-confirm-EndpointSet").css("display","block")
                            $('#dialog-confirm-EndpointSet').dialog
                              modal: true
                              buttons:
                                'Deklarieren': ->
                                        $(this).dialog 'close'
                                        
                                        #Delete all Interactions for this Storypoint
                                        removeAllInteractions(counter)
                                        #Delete all Edges from this Storypoint
                                        RemoveEdge(counter+"", false)
                                        #Delete all References at Storypoint - References
                                        storypointName = $("#inStorypoint_"+counter).val()
                                        if storypointName == ''
                                            storypointName = $("#inStorypoint_"+counter).attr("placeholder")
                                        removeAllShownGUIReferencesByName(storypointName, counter)
                                        
                                        #Disable the create Interaction button
                                        $("#btnCreateInteraction_" + counter).attr("disabled", true)
                                        $("#ddnInteractions_"+counter).attr("disabled",true)
                                        
                                        return
                                'Abbrechen': ->
                                        $(this).dialog 'close'
                                        document.getElementById("inEndOfStory_"+counter).checked = false
                                        return

                    else
                        $("#btnCreateInteraction_" + counter).attr("disabled", false)
                        $("#ddnInteractions_"+counter).attr("disabled",false)
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
                else if (activeTabID == "XMLTab") 

                    # Passive Content
                    $("#MedienEditor").css("display","none")
                    $("#GraEditor").css("display","none")
                    
                    # Passive Tabs
                    $("#GraEditorTab").removeClass("active")
                    $("#MedienBibTab").removeClass("active")
                    
                    # Active Tabs
                    $("#XMLTab").addClass("active")
                    # Active Content
                    $("#XML").css("display", "block")
                    $scope.codeMirrorUpdateUI =! $scope.codeMirrorUpdateUI

                    xml = startSynchro()
                    $scope.xmlFile = xml
                    
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

        # this will be initial executed and get all available story's
        $server.getStoryList (data) ->
            $scope.storys = data

        # Creates a story on the server
        $scope.createStory = () ->
            $server.createStory (id)->
                console.log id
                $location.path "/story/#{id}"


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


