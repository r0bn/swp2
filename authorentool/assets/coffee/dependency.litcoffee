            #Methode um GEORDNENT ALLE Storypoints zurückzugeben					
        getAllStorypoints = (buttonID, currentStorypointID) ->					
            prevStorypoint = $(currentStorypointID).prev().attr("id")				
            currentStorypoint = $(currentStorypointID).attr("id")			
            storypointArray = []					
            #find first prevStorypoint					
            while typeof prevStorypoint != 'undefined'					
                currentStorypoint = prevStorypoint					
                prevStorypoint = $("#" +prevStorypoint).prev().attr("id")				
            #push all from the lowest Storypoint into the array until it reached the highest					
            while typeof currentStorypoint != 'undefined' && currentStorypoint != "fgpStorypoint"	
                tmpStorypoint = currentStorypoint				
                storypointArray.push(tmpStorypoint)					
                currentStorypoint = $("#" +currentStorypoint).next().attr("id")					
            return storypointArray					


        selectAvailibleStorypoints = (storypointArray, currentStorypointID) ->
            index = storypointArray.indexOf(currentStorypointID)
            storypointArray.splice(index,1)
            return storypointArray



        setReferenceDropdownIDs = (node, lauf) ->
            node.children().each ->
                if typeof $(this).attr("id") != "undefined"
                    if $(this).attr("id").indexOf("_tmp_") != -1
                        split = $(this).attr("id").split("_tmp_")
                        newID = split[0] + "_" + window.dropdownLiCounter + "_" + lauf
                        $(this).attr("id", newID)
                    else 
                        newID = $(this).attr("id") + "_" + window.dropdownLiCounter + "_" + lauf
                        $(this).attr("id", newID)
                    lauf++;
                setReferenceDropdownIDs($(this), lauf)
            return


        helper = (storypointId, calcID) ->
            splitted = storypointId.split("_")
            return calcID = calcID + "_" + splitted[1]


        #OnTrue
        createDropdownQuizOnTrue = (counter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            storypointArray = getAllStorypoints(buttonID, "#"+currentStorypointID)

            storypointArray = selectAvailibleStorypoints(storypointArray, currentStorypointID)

            # Now all Storypoints as Objects are in the array: storypointArray
            
            copyForm = document.getElementById("liSkQuizOnTrueRef")
            window.dropdownLiCounter++;

            $("#ulSkQuizOnTrueRef_"+counter).empty()
            i = 0
            z = storypointArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_tmp_" + i
                stuff.style.display="block"
                
                if i == storypointArray.length
                    document.getElementById("ulSkQuizOnTrueRef_"+counter).appendChild(stuff)
                    tmpStoryname = "Ref löschen"
                else
                    inputID = helper(storypointArray[i], "inStorypoint")
                    document.getElementById("ulSkQuizOnTrueRef_"+counter).appendChild(stuff)
                    tmpStoryname = $("#" + inputID).val()
                    if tmpStoryname == ""
                        tmpStoryname = $("#" + inputID).attr("placeholder") 
                $("#" + stuff.id).find("a").attr("storypointOwner",storypointArray[i])
                $("#" + stuff.id).find("a").text(tmpStoryname)
                $("#" + stuff.id).find("a").html(tmpStoryname)
                i++

            # musste ich einbauen, damit die IDs der unterknoten nicht alle gleich sind, sondern verschieden. Das läuft
            lauf = 0
            setReferenceDropdownIDs($("#ulSkQuizOnTrueRef_" + counter), lauf)

            # Jetzt wurde für jedes DropdownMenu berechnet, wie viele Felder es beinhalten soll. Allerdings wird lediglich
            # nur die Schattenkopie von liSkQuizOnTrueRef angehängt. Jetzt werden noch die a - Felder befüllt
            # und zwar mit den Storypointnamen, und sollten die nicht gesetzt sein mit dem Platzhalter.

            # jetzt werden die Dropdownfelder klickbar gemacht und der richtige Name eingetragen. Hier macht er alles richtig
            # außer, dass er immer das click-event für ALLE ddnQuiz... übernimmt, anstatt eines EINEM zu geben - geht nicht
            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == storypointArray.length
                    $("#ddnQuizOnTrueStorypoint_"+indexe).click ->
                        $("#btnSetQuizOnTrueReferences_"+counter).val("Referenz neu setzen")
                        $("#btnSetQuizOnTrueReferences_"+counter).html("Referenz neu setzen <span class='caret' />")
                        return
                $("#ddnQuizOnTrueStorypoint_"+indexe).click ->
                    $("#btnSetQuizOnTrueReferences_"+counter).val($(this).text())
                    $("#btnSetQuizOnTrueReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetQuizOnTrueReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    storypoint_2 = storypoint_2.split("_")
                    AddEdge(storypoint[1], storypoint_2[1])
                    return


                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetQuizOnTrueReferences_"+ counter).val() == tmpStorypointValue
                        $("#btnSetQuizOnTrueReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetQuizOnTrueReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return

        #OnFalse
        createDropdownQuizOnFalse = (counter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            storypointArray = getAllStorypoints(buttonID, "#"+currentStorypointID)

            storypointArray = selectAvailibleStorypoints(storypointArray, currentStorypointID)
            
            copyForm = document.getElementById("liSkQuizOnFalseRef")
            window.dropdownLiCounter++;

            $("#ulSkQuizOnFalseRef_"+counter).empty()
            i = 0
            z = storypointArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_tmp_" + i
                stuff.style.display="block"
                
                if i == storypointArray.length
                    document.getElementById("ulSkQuizOnFalseRef_"+counter).appendChild(stuff)
                    tmpStoryname = "Ref löschen"
                else
                    inputID = helper(storypointArray[i], "inStorypoint")
                    document.getElementById("ulSkQuizOnFalseRef_"+counter).appendChild(stuff)
                    tmpStoryname = $("#" + inputID).val()
                    if tmpStoryname == ""
                        tmpStoryname = $("#" + inputID).attr("placeholder") 
                $("#" + stuff.id).find("a").attr("storypointOwner",storypointArray[i])
                $("#" + stuff.id).find("a").text(tmpStoryname)
                $("#" + stuff.id).find("a").html(tmpStoryname)
                i++

            lauf = 0
            setReferenceDropdownIDs($("#ulSkQuizOnFalseRef_" + counter), lauf)

            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == storypointArray.length
                    $("#ddnQuizOnFalseStorypoint_"+indexe).click ->
                        $("#btnSetQuizOnFalseReferences_"+counter).val("Referenz neu setzen")
                        $("#btnSetQuizOnFalseReferences_"+counter).html("Referenz neu setzen <span class='caret' />")
                        return
                $("#ddnQuizOnFalseStorypoint_"+indexe).click ->
                    $("#btnSetQuizOnFalseReferences_"+counter).val($(this).text())
                    $("#btnSetQuizOnFalseReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetQuizOnFalseReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    storypoint_2 = storypoint_2.split("_")
                    AddEdge(storypoint[1], storypoint_2[1])
                    return

                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetQuizOnFalseReferences_"+ counter).val() == tmpStorypointValue
                        $("#btnSetQuizOnFalseReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetQuizOnFalseReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return

        #Storypoint
        createDropdownStorypointRef = (counter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            storypointArray = getAllStorypoints(buttonID, "#"+currentStorypointID)

            storypointArray = selectAvailibleStorypoints(storypointArray, currentStorypointID)
            
            copyForm = document.getElementById("liSkStorypointRef")
            window.dropdownLiCounter++;

            $("#ulSkStorypointRef_"+counter).empty()
            i = 0
            z = storypointArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_tmp_" + i
                stuff.style.display="block"
                
                if i == storypointArray.length
                    document.getElementById("ulSkStorypointRef_"+counter).appendChild(stuff)
                    tmpStoryname = "Ref löschen"
                else
                    inputID = helper(storypointArray[i], "inStorypoint")
                    document.getElementById("ulSkStorypointRef_"+counter).appendChild(stuff)
                    tmpStoryname = $("#" + inputID).val()
                    if tmpStoryname == ""
                        tmpStoryname = $("#" + inputID).attr("placeholder")
                $("#" + stuff.id).find("a").attr("storypointOwner",storypointArray[i]) 
                $("#" + stuff.id).find("a").text(tmpStoryname)
                $("#" + stuff.id).find("a").html(tmpStoryname)
                i++

            lauf = 0
            setReferenceDropdownIDs($("#ulSkStorypointRef_" + counter), lauf)

            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == storypointArray.length
                    $("#ddnStorypointStorypoint_"+indexe).click ->
                        $("#btnSetStorypointReferences_"+counter).val("Referenz neu setzen")
                        $("#btnSetStorypointReferences_"+counter).html("Referenz neu setzen <span class='caret' />")
                        return
                $("#ddnStorypointStorypoint_"+indexe).click ->
                    $("#btnSetStorypointReferences_"+counter).val($(this).text())
                    $("#btnSetStorypointReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    storypoint_2 = storypoint_2.split("_")
                    AddEdge(storypoint[1], storypoint_2[1])
                    return

                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetStorypointReferences_"+ counter).val() == tmpStorypointValue
                        $("#btnSetStorypointReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetStorypointReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return


        edgeStorypointfinder = (objectID, searchedParent) ->
            found = false
            foundID = ""
            i = 0
            tmpObj = $(objectID)
            while found != true
                parentNode = tmpObj.parent()
                if typeof parentNode.attr("id") != "undefined"
                    tmp = parentNode.attr("id").split("_")
                    if tmp[0] == searchedParent
                        found = true
                        foundID = parentNode.attr("id")
                tmpObj = parentNode
            return foundID


        AddEdge = (StorypointID1, StorypointID2) ->
            edge = {
                from: StorypointID1,
                to: StorypointID2
            }
            window.edges.push(edge)
            container = document.getElementById('divDependencyBox');
            data = {
                nodes: window.nodes,
                edges: window.edges
            }
            network = new vis.Network(container, data, {});
            return

            #Methode zum hinzufügen und löschen von neuen Knoten wenn diese per createNewStorypoint neu angelegt werden
        AddoDeleteNewNodes = (nodeLabelInfo,searchID, counter) ->
            if searchID != ''
                i = 0
                while i < window.nodes.length
                    if window.nodes[i].nodeOwner == searchID 
                            window.nodes.splice(i,1);
                    i++
            else
                node = {
                        id: counter
                        label: nodeLabelInfo
                       }

                node.nodeOwner= "Storypoint_"+counter               
                window.nodes.push(node)
            container = document.getElementById('divDependencyBox');
            data = {
                nodes: window.nodes,
                edges: window.edges
            }
            network = new vis.Network(container, data, {});
            return

