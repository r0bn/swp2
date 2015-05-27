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
                #$scope.updateMediaFiles () ->
                #    console.log "ini"

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
        # Nodes and Edges for the dependency graph
        window.nodes = []
        window.edges = []
        window.interactioncounter = 10;
        initHelpSystem()
        initScrollbar()

        window.safeButtonCounter = 0
        initSafeButton()      

            
        $scope.createNewStorypoint = (counter) ->
                console.log "test"
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
                    if confirm "Wollen Sie den Storypoint wirklich löschen?"
                        window.safeButtonCounter--
                        checkSafeButton()
                        AddoDeleteNewNodes("",$("#fhlNeuerStorypoint_" + counter).attr("nodeOwner"), counter)
                        $("#fhlNeuerStorypoint_" + counter).toggle "drop", 200, () ->
                            $("#fhlNeuerStorypoint_" + counter).remove()
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
                if counter > 6
                    $("#divHelpBox").html('<iframe width="'+0+'" height="'+0+'" src="https://www.youtube.com/embed/lTcRfJyEKkM?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>');
                    
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

                console.log "test"
        
   

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
                    xml = startSynchro()
                    # Active Content
                    $("#XML").css("display", "block")
                    $("#ttaXML").val(xml)
                    cm = CodeMirror.fromTextArea($("#XML").find('textarea')[0],
                      lineNumbers: true
                      matchBrackets: true)

                    setMode(cm, "xml");
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


