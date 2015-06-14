
        createNewStorypointX = (counter) ->
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
