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


        #QuizContent
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
                        storypoint = edgeStorypointfinder("#btnSetQuizOnTrueReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        oldEdge = $("#btnSetQuizOnTrueReferences_"+counter).attr("oldValue")
                        if typeof oldEdge != "undefined"
                            oldEdge = oldEdge.split("_")
                            RemoveParticularEdge(storypoint[1], oldEdge[1])
                        $("#btnSetQuizOnTrueReferences_"+counter).attr("oldValue", "undefined")
                        $("#btnSetQuizOnTrueReferences_"+counter).attr("currentEdge", "undefined")
                        $("#btnSetQuizOnTrueReferences_"+counter).val("Neue Ref setzen")
                        $("#btnSetQuizOnTrueReferences_"+counter).html("Neue Ref setzen <span class='caret' />")
                        return
                else $("#ddnQuizOnTrueStorypoint_"+indexe).click ->
                    $("#btnSetQuizOnTrueReferences_"+counter).val($(this).text())
                    $("#btnSetQuizOnTrueReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetQuizOnTrueReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    $("#btnSetQuizOnTrueReferences_"+counter).attr("currentEdge", storypoint_2)
                    storypoint_2 = storypoint_2.split("_")
                    oldEdge = $("#btnSetQuizOnTrueReferences_"+counter).attr("oldValue")
                    if typeof oldEdge != "undefined"
                        oldEdge = oldEdge.split("_")
                        RemoveParticularEdge(storypoint[1], oldEdge[1])
                    AddEdge(storypoint[1], storypoint_2[1])
                    return


                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetQuizOnTrueReferences_"+ counter).val() == tmpStorypointValue
                        storypoint = edgeStorypointfinder("#btnSetQuizOnTrueReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        # RemoveEdge(storypoint[1])
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
                        storypoint = edgeStorypointfinder("#btnSetQuizOnFalseReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        oldEdge = $("#btnSetQuizOnFalseReferences_"+counter).attr("oldValue")
                        if typeof oldEdge != "undefined"
                            oldEdge = oldEdge.split("_")
                            RemoveParticularEdge(storypoint[1], oldEdge[1])
                        $("#btnSetQuizOnFalseReferences_"+counter).attr("oldValue", "undefined")
                        $("#btnSetQuizOnFalseReferences_"+counter).attr("currentEdge", "undefined")
                        $("#btnSetQuizOnFalseReferences_"+counter).val("Neue Ref setzen")
                        $("#btnSetQuizOnFalseReferences_"+counter).html("Neue Ref setzen <span class='caret' />")
                        return
                else $("#ddnQuizOnFalseStorypoint_"+indexe).click ->
                    $("#btnSetQuizOnFalseReferences_"+counter).val($(this).text())
                    $("#btnSetQuizOnFalseReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetQuizOnFalseReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    $("#btnSetQuizOnFalseReferences_"+counter).attr("currentEdge", storypoint_2)
                    storypoint_2 = storypoint_2.split("_")
                    oldEdge = $("#btnSetQuizOnFalseReferences_"+counter).attr("oldValue")
                    if typeof oldEdge != "undefined"
                        oldEdge = oldEdge.split("_")
                        RemoveParticularEdge(storypoint[1], oldEdge[1])
                    AddEdge(storypoint[1], storypoint_2[1])
                    return

                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetQuizOnFalseReferences_"+ counter).val() == tmpStorypointValue
                        storypoint = edgeStorypointfinder("#btnSetQuizOnFalseReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        # RemoveEdge(storypoint[1])
                        $("#btnSetQuizOnFalseReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetQuizOnFalseReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return
            
            
        #ChooserContent
        
        #Storypoint
        createChooserStorypointReference = (counter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            storypointArray = getAllStorypoints(buttonID, "#"+currentStorypointID)

            storypointArray = selectAvailibleStorypoints(storypointArray, currentStorypointID)
            
            copyForm = document.getElementById("liSkChooserStorypointRef")
            window.dropdownLiCounter++;

            $("#ulSkChooserStorypointRef_"+counter).empty()
            i = 0
            z = storypointArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_tmp_" + i
                stuff.style.display="block"
                
                if i == storypointArray.length
                    document.getElementById("ulSkChooserStorypointRef_"+counter).appendChild(stuff)
                    tmpStoryname = "Ref löschen"
                else
                    inputID = helper(storypointArray[i], "inStorypoint")
                    document.getElementById("ulSkChooserStorypointRef_"+counter).appendChild(stuff)
                    tmpStoryname = $("#" + inputID).val()
                    if tmpStoryname == ""
                        tmpStoryname = $("#" + inputID).attr("placeholder") 
                $("#" + stuff.id).find("a").attr("storypointOwner",storypointArray[i])
                $("#" + stuff.id).find("a").text(tmpStoryname)
                $("#" + stuff.id).find("a").html(tmpStoryname)
                i++

            lauf = 0
            setReferenceDropdownIDs($("#ulSkChooserStorypointRef_" + counter), lauf)

            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == storypointArray.length
                    $("#ddnChooserStorypoint_"+indexe).click ->
                        storypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        oldEdge = $("#btnSetChooserStorypointReferences_"+counter).attr("oldValue")
                        if typeof oldEdge != "undefined"
                            oldEdge = oldEdge.split("_")
                            RemoveParticularEdge(storypoint[1], oldEdge[1])
                        $("#btnSetChooserStorypointReferences_"+counter).attr("oldValue", "undefined")
                        $("#btnSetChooserStorypointReferences_"+counter).attr("currentEdge", "undefined")
                        $("#btnSetChooserStorypointReferences_"+counter).val("Neue Ref setzen")
                        $("#btnSetChooserStorypointReferences_"+counter).html("Neue Ref setzen <span class='caret' />")
                        return
                else $("#ddnChooserStorypoint_"+indexe).click ->
                    $("#btnSetChooserStorypointReferences_"+counter).val($(this).text())
                    $("#btnSetChooserStorypointReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    storypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_"+counter, "fhlNeuerStorypoint" )
                    storypoint = storypoint.split("_")
                    storypoint_2 = $(this).attr("storypointOwner")
                    $("#btnSetChooserStorypointReferences_"+counter).attr("currentEdge", storypoint_2)
                    storypoint_2 = storypoint_2.split("_")
                    oldEdge = $("#btnSetChooserStorypointReferences_"+counter).attr("oldValue")
                    if typeof oldEdge != "undefined"
                        oldEdge = oldEdge.split("_")
                        RemoveParticularEdge(storypoint[1], oldEdge[1])
                    AddEdge(storypoint[1], storypoint_2[1])
                    return

                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetChooserStorypointReferences_"+ counter).val() == tmpStorypointValue
                        storypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_"+counter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        RemoveEdge(storypoint[1])
                        $("#btnSetChooserStorypointReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetChooserStorypointReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return
            
            
            #Item
            
        getAllItems = () ->
            
            itemArray = []
            i = 10; 
            itemIndex = 11
            while i < window.interactioncounter
                tempItem = $("#fgpNeu_Item_"+itemIndex).attr("id")
                if typeof tempItem == 'undefined'	
                    itemIndex++
                    i++
                    continue
                else
                    itemArray.push(tempItem)
            
                itemIndex++
                i++
            
            return itemArray
            
            
        itemHelper = (storypointId, calcID) ->
            splitted = storypointId.split("_")
            return calcID = calcID + "_" + splitted[2]
            
            
            
            
        createChooserItemReference = (counter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            itemArray = getAllItems()
            
            copyForm = document.getElementById("liSkChooserItemRef")
            window.dropdownLiCounter++;

            $("#ulSkChooserItemRef_"+counter).empty()
            i = 0
            z = itemArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_tmp_" + i
                stuff.style.display="block"
                
                if i == itemArray.length
                    document.getElementById("ulSkChooserItemRef_"+counter).appendChild(stuff)
                    tmpItemname = "Ref löschen"
                else
                    inputID = itemHelper(itemArray[i], "inItemID")
                    document.getElementById("ulSkChooserItemRef_"+counter).appendChild(stuff)
                    tmpItemname = $("#" + inputID).val()
                    if tmpItemname == ""
                        tmpItemname = $("#" + inputID).attr("placeholder") 
                $("#" + stuff.id).find("a").attr("itemOwner",itemArray[i])
                $("#" + stuff.id).find("a").text(tmpItemname)
                $("#" + stuff.id).find("a").html(tmpItemname)
                i++

            lauf = 0
            setReferenceDropdownIDs($("#ulSkChooserItemRef_" + counter), lauf)

            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == itemArray.length
                    $("#ddnChooserItem_"+indexe).click ->
                        #storypoint = edgeStorypointfinder("#btnSetChooserItemReferences_"+counter, "fhlNeuerStorypoint" )
                        #storypoint = storypoint.split("_")
                        #RemoveEdge(storypoint[1])
                        $("#btnSetChooserItemReferences_"+counter).val("Neue Ref setzen")
                        $("#btnSetChooserItemReferences_"+counter).html("Neue Ref setzen <span class='caret' />")
                        return
                else $("#ddnChooserItem_"+indexe).click ->
                    $("#btnSetChooserItemReferences_"+counter).val($(this).text())
                    $("#btnSetChooserItemReferences_"+counter).html($(this).text() + "<span class='caret' />")
                    #storypoint = edgeStorypointfinder("#btnSetChooserItemReferences_"+counter, "fhlNeuerStorypoint" )
                    #storypoint = storypoint.split("_")
                    #storypoint_2 = $(this).attr("itemOwner")
                    #storypoint_2 = storypoint_2.split("_")
                    #RemoveEdge(storypoint[1])
                    #AddEdge(storypoint[1], storypoint_2[1])
                    return

                $("#" + itemArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpItemValue = $("#inItemID_" + tmpCounter).val()
                    if tmpItemValue == ''
                        tmpItemValue = $("#inItemID_" + tmpCounter).attr("placeholder")

                    if $("#btnSetChooserItemReferences_"+ counter).val() == tmpItemValue
                        #storypoint = edgeStorypointfinder("#btnSetChooserItemReferences_"+counter, "fhlNeuerStorypoint" )
                        #storypoint = storypoint.split("_")
                        #RemoveEdge(storypoint[1])
                        $("#btnSetChooserItemReferences_" + counter).val("Neue Ref setzen")
                        $("#btnSetChooserItemReferences_" + counter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++
            return
            


        # Erstellt die Logik hinter den Storypoint Reference Dropdownlisten
        createDropdownStorypointRef = (counter,rowCounter, columnCounter, buttonID, currentObjID) ->

            currentStorypointID = $(currentObjID).closest(".form-horizontal").attr("id")
            storypointArray = []
            storypointArray = getAllStorypoints(buttonID, "#"+currentStorypointID)

            storypointArray = selectAvailibleStorypoints(storypointArray, currentStorypointID)
            
            copyForm = document.getElementById("liSkStorypointRef")
            window.dropdownLiCounter++;

            $("#ulStorypointRef_"+counter + "_" +rowCounter + "_" + columnCounter).empty()
            i = 0
            z = storypointArray.length
            z++
            while i < z
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_" +counter + "_" +rowCounter + "_" + columnCounter
                setIDs($("#" + stuff.id), counter)
                setIDs($("#" + stuff.id), rowCounter)
                setIDs($("#" + stuff.id), columnCounter)
                stuff.id = stuff.id + "_tmp_" + i

                stuff.style.display="block"
                
                if i == storypointArray.length
                    document.getElementById("ulStorypointRef_"+counter + "_" +rowCounter + "_" + columnCounter).appendChild(stuff)
                    tmpStoryname = "Ref löschen"
                else
                    inputID = helper(storypointArray[i], "inStorypoint")
                    if typeof $("#ulStorypointRef_"+counter + "_" +rowCounter + "_" + columnCounter) != "undefined"
                        document.getElementById("ulStorypointRef_"+counter + "_" +rowCounter + "_" + columnCounter).appendChild(stuff)
                        tmpStoryname = $("#" + inputID).val()
                        if tmpStoryname == ""
                            tmpStoryname = $("#" + inputID).attr("placeholder")
                $("#" + stuff.id).find("a").attr("id",$("#" + stuff.id).find("a").attr("id") + "_"+counter + "_" +rowCounter + "_" + columnCounter) 
                $("#" + stuff.id).find("a").attr("storypointOwner",storypointArray[i]) 
                $("#" + stuff.id).find("a").text(tmpStoryname)
                $("#" + stuff.id).find("a").html(tmpStoryname)
                i++

            lauf = 0

            setReferenceDropdownIDs($("#ulStorypointRef_" +counter + "_" +rowCounter + "_" + columnCounter), lauf)

            j = 0
            while j < z
                indexe = window.dropdownLiCounter + "_" + (j+1)
                if j == storypointArray.length
                    $("#ddnStorypointStorypoint_"+counter + "_" +rowCounter + "_" + columnCounter + "_" +indexe).click ->
                        storypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("selectedOwner")
                        prevColumn = columnCounter
                        prevColumn--
                        storypoint = storypoint.split("_")
                        if prevColumn > 0
                            previousStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + prevColumn).attr("selectedOwner")
                            previousStorypoint = previousStorypoint.split("_")
                            RemoveParticularEdge(storypoint[1], previousStorypoint[1])
                        else 
                            previousStorypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, "fhlNeuerStorypoint" )
                            previousStorypoint = previousStorypoint.split("_")
                            RemoveParticularEdge(storypoint[1],previousStorypoint[1])
                        i = columnCounter
                        i++
                        button = document.getElementById("btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter)
                        length = button.parentNode.parentNode.parentNode.childNodes.length
                        while i < length
                           nextStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("selectedOwner")
                           if typeof nextStorypoint != "undefined"
                                nextStorypoint = nextStorypoint.split("_")
                                RemoveParticularEdge(nextStorypoint[1], storypoint[1])
                                storypoint = nextStorypoint
                           $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("selectedOwner", "undefined")
                           $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).val("Neue Ref setzen")
                           $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).html("Neue Ref setzen <span class='caret' />")
                           i++
                        
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("selectedOwner", "undefined")
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).val("Neue Ref setzen")
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).html("Neue Ref setzen <span class='caret' />")
                        return
                else $("#ddnStorypointStorypoint_"+counter + "_" +rowCounter + "_" + columnCounter + "_" +indexe).click ->
                    $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("selectedOwner", $(this).attr("storypointOwner"))
                    $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).val($(this).text())
                    $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).html($(this).text() + "<span class='caret' />")
                    
                    copyForm = document.getElementById("colStorypointRefDeep")
                    button = document.getElementById("btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter)
                    if button.parentNode.parentNode.parentNode.childNodes.length < 4
                        newStorypointRef = copyForm.cloneNode(true)
                        copyItemForm = document.getElementById("colItemRefDeep")
                        newItemRef = copyItemForm.cloneNode(true)
                        newElemCounter = columnCounter
                        newElemCounter++
                        newStorypointRef.id = "colStorypointRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter
                        newItemRef.id = "colItemRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter
                        
                        button.parentNode.parentNode.parentNode.insertBefore(newStorypointRef, button.parentNode.parentNode.nextSibling)
                        button.parentNode.parentNode.parentNode.nextSibling.appendChild(newItemRef)
                        setIDs($("#colStorypointRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), counter)
                        setIDs($("#colStorypointRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), rowCounter)
                        setIDs($("#colStorypointRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), newElemCounter)
                        setIDs($("#colItemRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), counter)
                        setIDs($("#colItemRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), rowCounter)
                        setIDs($("#colItemRefDeep_"+counter + "_" +rowCounter + "_" + newElemCounter), newElemCounter)
                        createDropdownStorypointRef(counter,rowCounter,newElemCounter, "#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + newElemCounter, currentObjID)
                        $("#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + newElemCounter).click ->
                            createDropdownStorypointRef(counter,rowCounter,newElemCounter, "#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + newElemCounter, currentObjID)
                            return
                    oldEdgeStartpoint
                    previousColumn = columnCounter
                    nextColumn = columnCounter
                    previousColumn--
                    nextColumn++
                    previousStorypoint
                    if columnCounter == 1
                        previousStorypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, "fhlNeuerStorypoint" )
                    else
                        previousStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + previousColumn).attr("selectedOwner")
                    nextStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + nextColumn).attr("selectedOwner")
                    previousStorypoint = previousStorypoint.split("_")
                    if typeof $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("oldEdgeStartpoint") != "undefined"
                        oldEdgeStartpoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("oldEdgeStartpoint")
                        oldEdgeStartpoint = oldEdgeStartpoint.split("_")
                        RemoveParticularEdge(oldEdgeStartpoint[1], previousStorypoint[1])
                    storypoint = $(this).attr("storypointOwner")
                    oldStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("oldEdgeStartpoint")
                    currentStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("selectedOwner")
                    if typeof currentStorypoint != "undefined"
                        $("#" + currentStorypoint).on "remove.btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, ->
                            prevColumn = columnCounter
                            prevColumn--
                            currentStorypoint = currentStorypoint.split("_")
                            if prevColumn > 0
                                previousStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + prevColumn).attr("selectedOwner")
                                previousStorypoint = previousStorypoint.split("_")
                                RemoveParticularEdge(currentStorypoint[1], previousStorypoint[1])
                            else 
                                previousStorypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, "fhlNeuerStorypoint" )
                                previousStorypoint = previousStorypoint.split("_")
                                RemoveParticularEdge(currentStorypoint[1],previousStorypoint[1])
                            i = columnCounter
                            i++
                            button = document.getElementById("btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter)
                            length = button.parentNode.parentNode.parentNode.childNodes.length
                            while i < length
                               nextStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("selectedOwner")
                               if typeof nextStorypoint != "undefined"
                                    nextStorypoint = nextStorypoint.split("_")
                                    RemoveParticularEdge(nextStorypoint[1], currentStorypoint[1])
                                    currentStorypoint = nextStorypoint
                               $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("oldEdgeStartpoint", "undefined")
                               $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("selectedOwner", "undefined")
                               $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).val("Neue Ref setzen")
                               $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).html("Neue Ref setzen <span class='caret' />")
                               i++
                            $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("oldEdgeStartpoint", "undefined")
                            $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("selectedOwner", "undefined")
                            $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).val("Neue Ref setzen")
                            $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).html("Neue Ref setzen <span class='caret' />")
                            return
                        if typeof oldStorypoint != "undefined"
                            $("#" + oldStorypoint).unbind("remove.btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter)
                            
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).attr("oldEdgeStartpoint", storypoint)
                        storypoint = storypoint.split("_")
                        AddEdge(storypoint[1], previousStorypoint[1])
                        if typeof nextStorypoint != "undefined"
                           nextStorypoint = nextStorypoint.split("_")
                           RemoveParticularEdge(nextStorypoint[1], oldEdgeStartpoint[1])
                           AddEdge(nextStorypoint[1], storypoint[1])
                    return

                $("#" + storypointArray[j]).find("button:nth-child(4)").click ->
                    tmpCounter = $(this).attr("id").split("_")[1]
                    tmpStorypointValue = $("#inStorypoint_" + tmpCounter).val()
                    if tmpStorypointValue == ''
                        tmpStorypointValue = $("#inStorypoint_" + tmpCounter).attr("placeholder")

                    if $("#btnSetStorypointReferences_"+ counter).val() == tmpStorypointValue
                        storypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, "fhlNeuerStorypoint" )
                        storypoint = storypoint.split("_")
                        # RemoveEdge(storypoint[1])
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).val("Neue Ref setzen")
                        $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter).html("Neue Ref setzen <span class='caret' />")
                    return
                j++  
            return
        
        # Findet den Storypoint zu dem das Object gehört            
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

        # Fügt eine neue Kante hinzu
        AddEdge = (FromStorypoint, ToStorypoint) ->
            if typeof FromStorypoint == "undefined" || typeof ToStorypoint == "undefined"
                return
            else
                i = 0
                found = -1
                while i < window.edges.length
                    if window.edges[i].from == FromStorypoint && window.edges[i].to == ToStorypoint
                        found = i
                    i++
                if found > -1
                    edge = {
                        from: FromStorypoint,
                        to: ToStorypoint,
                        arrows: 'to'
                    }
                    window.duplicateEdges.push(edge)
                else
                    edge = {
                        from: FromStorypoint,
                        to: ToStorypoint,
                        arrows: 'to'
                    }
                    window.edges.push(edge)
                    container = document.getElementById('divDependencyBox');
                    data = {
                        nodes: window.nodes,
                        edges: window.edges
                    }
                    network = new vis.Network(container, data, {});
                    return

        # Findet einen Knoten anhand seiner ID und löscht alle Verbindungen die von ihm weg gingen
        # Wenn nodeRemoved gesetzt ist (true), dann werden alle Referenzen gelöscht
        RemoveEdge = (StorypointID, nodeRemoved) ->
            i = 0
            while i < window.edges.length
                if window.edges[i].from == StorypointID
                    window.edges.splice(i,1);
                
                if nodeRemoved == true && typeof window.edges[i] != "undefined" && window.edges[i].to == StorypointID
                    window.edges.splice(i,1);
                i++
            i = 0
            while i < window.duplicateEdges.length
                if window.duplicateEdges[i].from == StorypointID
                    window.duplicateEdges.splice(i,1);
                
                if nodeRemoved == true && typeof window.duplicateEdges[i] != "undefined" && window.duplicateEdges[i].to == StorypointID
                    window.duplicateEdges.splice(i,1);
                i++
            container = document.getElementById('divDependencyBox');
            data = {
                nodes: window.nodes,
                edges: window.edges
            }
            network = new vis.Network(container, data, {});
            return
            
        # Fügt ein Item einer Kante hinzu. Sollte es keine Kante geben, wird diese neu erstellt
        addItemToEdge = (FromStorypoint, ToStorypoint, ItemLabel) ->
            if typeof FromStorypoint == "undefined" || typeof ToStorypoint == "undefined" || typeof ItemLabel == "undefined"
                return
            else
                i = 0
                found = -1
                while i < window.edges.length
                    if window.edges[i].from == FromStorypoint && window.edges[i].to == ToStorypoint
                        found = i
                    i++
                if found > -1
                    window.edges[found].label = ItemLabel
                    i = 0
                    found = -1
                    while i < window.duplicateEdges.length
                        if window.duplicateEdges[i].from == FromStorypoint && window.duplicateEdges[i].to == ToStorypoint
                            window.duplicateEdges[found].label = ItemLabel
                        i++
                else
                    edge = {
                        from: FromStorypoint,
                        to: ToStorypoint,
                        label: ItemLabel,
                        font: {align: 'top'},
                        arrows: 'to'
                    }
                    window.edges.push(edge)
                container = document.getElementById('divDependencyBox');
                data = {
                    nodes: window.nodes,
                    edges: window.edges
                }
                network = new vis.Network(container, data, {});
                return
        # Löscht eine bestimmte Kante aus dem Graph
        RemoveParticularEdge = (FromStorypoint, ToStorypoint) ->
            if typeof FromStorypoint == "undefined" || typeof ToStorypoint == "undefined"
                return
            else
                i = 0
                found = -1
                while i < window.duplicateEdges.length
                    if window.duplicateEdges[i].from == FromStorypoint && window.duplicateEdges[i].to == ToStorypoint
                        found = i
                        i = window.duplicateEdges.length
                    i++
                if found > -1
                    window.duplicateEdges.splice(found,1);
                else
                    i = 0
                    while i < window.edges.length
                        if window.edges[i].from == FromStorypoint && window.edges[i].to == ToStorypoint
                            window.edges.splice(i,1);
                            i = window.edges.length
                        i++ 
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
                splitted = searchID.split("_")
                RemoveEdge(splitted[1], true)
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

        # Methode fügt eine neue Oder verknüpfte Zeile zu den Storypoint Referenzen hinzu.
        createStorypointRef = (counter, rowCounter, storypointID) ->
            button = document.getElementById("btnCreateReferences_" + counter)
            columnCounter = 1
            storyPointRefOrig = document.getElementById('fgpMultipleStorypointRefs')
            storyPointRef = storyPointRefOrig.cloneNode(true)
            storyPointRef.id = 'fgpMultipleStorypointRefs_' + counter + "_" + rowCounter
            storyPointRef.style.display = "block"
            button.parentNode.parentNode.parentNode.insertBefore(storyPointRef, button.parentNode.parentNode)
            setIDs($('#fgpMultipleStorypointRefs_' + counter + "_" + rowCounter), counter)
            setIDs($('#fgpMultipleStorypointRefs_' + counter + "_" + rowCounter), rowCounter)
            setIDs($('#fgpMultipleStorypointRefs_' + counter + "_" + rowCounter), columnCounter)
            
            itemRefOrig = document.getElementById('fgpMultipleItemRefs')
            itemRef = itemRefOrig.cloneNode(true)
            itemRef.id = 'fgpMultipleItemRefs_' + counter + "_" + rowCounter
            itemRef.style.display = "block"
            storyPointRef.parentNode.insertBefore(itemRef, storyPointRef.nextSibling)
            setIDs($('#fgpMultipleItemRefs_' + counter + "_" + rowCounter), counter)
            setIDs($('#fgpMultipleItemRefs_' + counter + "_" + rowCounter), rowCounter)
            setIDs($('#fgpMultipleItemRefs_' + counter + "_" + rowCounter), columnCounter)
            createDropdownStorypointRef(counter,rowCounter,columnCounter, "#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + columnCounter, storypointID)
            $("#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + columnCounter).click ->
                createDropdownStorypointRef(counter,rowCounter,columnCounter, "#btnSetStorypointReferences_" + counter + "_" + rowCounter + "_" + columnCounter, storypointID)
                return
            $("#btnStorypointRefDelete_"+counter + "_" +rowCounter + "_" + columnCounter).click ->
                $("#dialog-confirm-Referenzen").css("display","block")
                $('#dialog-confirm-Referenzen').dialog
                  modal: true
                  buttons:
                    'Löschen': ->
                            $(this).dialog 'close'
                            storypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_1").attr("selectedOwner")
                            if typeof storypoint != "undefined"
                                storypoint = storypoint.split("_")
                                previousStorypoint = edgeStorypointfinder("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + columnCounter, "fhlNeuerStorypoint" )
                                previousStorypoint = previousStorypoint.split("_")
                                RemoveParticularEdge(storypoint[1],previousStorypoint[1])
                                i = 1
                                i++
                                while i < 4
                                   nextStorypoint = $("#btnSetStorypointReferences_"+counter + "_" +rowCounter + "_" + i).attr("selectedOwner")
                                   if typeof nextStorypoint != "undefined" && typeof storypoint != "undefined"
                                       nextStorypoint = nextStorypoint.split("_")
                                       RemoveParticularEdge(nextStorypoint[1], storypoint[1])
                                   storypoint = nextStorypoint
                                   i++
                            $("#fgpMultipleStorypointRefs_" + counter + "_" + rowCounter).toggle "drop", 200, () ->
                                $("#fgpMultipleStorypointRefs_" + counter + "_" + rowCounter).remove()
                            $("#fgpMultipleItemRefs_" + counter + "_" + rowCounter).toggle "drop", 200, () ->
                                $("#fgpMultipleItemRefs_" + counter + "_" + rowCounter).remove() 
                            return
                    'Abbrechen': ->
                            $(this).dialog 'close'
                            return
                    
                return
            return
            
            
            
            
            
        checkPlayableStory = () ->
            
            foundStartpoint = false
            graphOK = false
            foundEndpoint = false
            
            #Methods to test if a Story is playable
            foundStartpoint = checkStartPointSearch()
            
            if !foundStartpoint
                alert "Story hat keinen Startpunkt"
                return
            #if foundStartpoint now is true, that means that there is at least 1 startPoint, anywhere.
            
            #Check if All Story-Endpoints are reachable from an Startpoint
            
            reachableEndpoints = checkEndpointFromStartpoint()
            
            if !reachableEndpoints
                alert "Story kann nicht zuende gespielt werden. Ein Endpunkt ist nicht von einem Startpunkt erreichbar"
                return
                
                
            #checkCommand = "Die Story ist nicht spielbar"
            #if foundStartpoint && reachableEndpoints
                #checkCommand = "Die Story ist spielbar"
                
            alert "Die Story ist spielbar!"
            return 
            
            
            
        #Method to check if a Story has at least one Startpoint
        checkStartPointSearch = () ->
            storypointCheckArray = []
            i = 0
            while i < window.nodes.length
                storyPointTo = window.nodes[i]
                storyPointToId = storyPointTo.id + ""
                storypointCheckArray.push(checkStorypointToPointer(storyPointToId))
                i++

            checkStartpoint = false
            i = 0
            while i < storypointCheckArray.length
                if !storypointCheckArray[i]
                    checkStartpoint = true
                    break
                i++
                
            #Sofern checkStartpoint true ist, gibt es mindestens einen Startpunkt und die Story ist
            #in dieser Hinsicht spielbar. Sofern checkStartpoint false ist, ist die Story gar nicht spielbar
            return checkStartpoint


        #Gibt zu einem Storypoint zurueck, ob dieser einen Pfeil auf sich hat (true) oder nicht (false)
        #Sofern er keinen Pfeil auf sich hat, ist es ein moeglicher Startpunkt der Story
        #Also man kann von diesem Punkt aus die Story starten
        checkStorypointToPointer = (storyPointToId) ->
            j = 0
            while j < window.edges.length
                if window.edges[j].to == storyPointToId
                    return true #Heißt: Der Punkt x hat ein Pfeil auf sich zeigend
                j++
            return false #Heißt: Der Punkt x hat keinen Pfeil der auf sich zeigt
            
            
            
            
        
        #Methode zum zu ueberpruefen ob ein Endpunkt von einem Startpunkt erreichbar ist
        checkEndpointFromStartpoint = () ->

            reachableEndpoint = false
            endpointStorypointArray = []
            endpointStorypointArray = getAllEndpointStorypoints()
            
            if endpointStorypointArray.length == 0
                alert "Story hat keinen Endpunkt"
                return false
                
                #return "Story hat keinen Endpunkt"
            
            
            #Now all Endstorypoints are known. Now test if you can reach them from a startPoint
            startpointStorypointArray = []
            i = 0
            while i < window.storypointCounter
                i++
                tempStorypointId = $("#fhlNeuerStorypoint_"+i).attr("id")
                if typeof tempStorypointId != 'undefined'
                    #if the next if-statement is false, the storypoint has NO references of it and is a startpoint => Add it.
                    tempStorypointId = tempStorypointId.split("_")[1]
                    if !checkStorypointToPointer(tempStorypointId)
                        startpointStorypointArray.push(tempStorypointId)
                        
            #Now all Startstorypoints are known. Now test if you can reach a endStorypoint from a startStorypoint
            
            reachableEndpoint = findPath(startpointStorypointArray, endpointStorypointArray)
            return reachableEndpoint
            
            
        #Methode to calculate if an Endstorypoint is reachable from a Startstorypoint
        findPath = (startpointStorypointArray, endpointStorypointArray) ->
        
            endstorypointCounter = 0
            while endstorypointCounter < endpointStorypointArray.length
                
                #get All Points that points to this spezial Point.
                pointerPathArrayToEndstorypoint = []
                pointerPathArrayToEndstorypoint = getAllStorypointsWhichPointsOnGiven(endpointStorypointArray[endstorypointCounter])
                
                
                #Testen, ob pointerPathArrayToEndstorypoint.length == 0, wenn ja, kann man beim Endpunkt auch anfangen und ist somit
                #sofort fertig => gültige Story
                if pointerPathArrayToEndstorypoint.length == 0
                    if startpointStorypointArray.indexOf(endpointStorypointArray[endstorypointCounter])
                        return true
                
                
                #Alle Vorgänger prüfen:
                precursorStorypointCounter = 0
                while precursorStorypointCounter < pointerPathArrayToEndstorypoint.length
                    
                    if startpointStorypointArray.indexOf(pointerPathArrayToEndstorypoint[precursorStorypointCounter]) >= 0
                        return true
                    else 
                        #rekursiver aufruf, ob es einen Weg ab dem VORGÄNGER des Vorgängers zu einem Startpunkt gibt. 
                        #Hier wird einfach rekursive die Methode wieder aufgerufen OHNE etwas zu returnen, da hier nicht schon
                        # die Methode abbrechen darf, ohne dass die anderen Endstorypoints geprüft worden sind, sofern es
                        # bei diesem einem KEINEN Startstorypoint gibt.
                        
                        fakeEndpointStorypointArray = []
                        fakeEndpointStorypointArray.push(pointerPathArrayToEndstorypoint[precursorStorypointCounter])
                        
                        if !findPath(startpointStorypointArray, fakeEndpointStorypointArray)
                            precursorStorypointCounter++
                            continue
                        else
                            return true

                        #return true
                    
                    precursorStorypointCounter++
                
                endstorypointCounter++
            return false
            
            
            
        #Methode that returns all Storypoints that references on a given Storypoint
        getAllStorypointsWhichPointsOnGiven = (givenStorypointId) ->
            
            storypointToStorypointArray = []            
            j = 0
            while j < window.edges.length
                if window.edges[j].to == givenStorypointId
                    storypointToStorypointArray.push(window.edges[j].from)
                j++

            return storypointToStorypointArray
            
            
        #This Methode returns all Storypoints which are Endpoints too.
        getAllEndpointStorypoints = () ->
        
            endpointStorypointArray = []
            i = 0 
            while i < window.storypointCounter
                i++
                tempStorypointId = $("#fhlNeuerStorypoint_"+ i).attr("id")
                if typeof tempStorypointId != 'undefined'
                    if document.getElementById("inEndOfStory_"+i).checked
                        tempStorypointId = tempStorypointId.split("_")[1]
                        endpointStorypointArray.push(tempStorypointId)
            
            return endpointStorypointArray
            
            
            
            
            
            
            
            
            
            
            
            
