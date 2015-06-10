                
        createItem = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())          
            window.interactioncounter++
            interactionCounter = window.interactioncounter
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            setIDs($("#" + stuff.id), interactionCounter)
            $("#inItemID_" + interactionCounter).attr("placeholder", $("#inItemID_" + interactionCounter).attr("placeholder") + interactionCounter)
            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)
            # Click Event für btnItemDelete
            $("#btnItemDelete_" + interactionCounter).click ->
                $("#dialog-confirm-Item").css("display","block")
                $('#dialog-confirm-Item').dialog
                  modal: true
                  buttons:
                    'Löschen': ->
                            $(this).dialog 'close'
                            $("#fgpNeu_Item_" + interactionCounter).toggle "drop", 200, () ->
                                $("#fgpNeu_Item_" + interactionCounter).remove()
                            return
                    'Abbrechen': ->
                            $(this).dialog 'close'
                            return

            # Click Event für btnItemEinklappen
            btnEinklappen("#btnItemEinklappen_" + interactionCounter, "#fstNeuesItemContent_" + interactionCounter)
            
            # ItemName Trigger
            $("#inItemID_"+interactionCounter).keyup ->
                inIDSetter($("#inItemID_"+interactionCounter), $("#lgdNeuesItemFieldset_"+interactionCounter), "Item: ", "Neues Item")

            # inItemStorypointRef Text setzen
            $("#inItemStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
            # inItemStorypointRef Trigger
            $("#inStorypoint_" + counter).on 'input', () ->
                objectInput = $("#inItemStorypointRef_" + interactionCounter)
                objectInput.val($("#inStorypoint_" + counter).val())
                if typeof objectInput.val() != "undefined"
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
            helpRek( $("#" + stuff.id))

        createQuiz = (counter) ->
            copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
            window.interactioncounter++
            interactionCounter = window.interactioncounter
            stuff = copyForm.cloneNode(true)
            stuff.id = stuff.id + "_" + interactionCounter
            stuff.style.display="block"
            document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
            setIDs($("#" + stuff.id), interactionCounter)
            btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
            btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)
            

            # Click Event für btnQuizDelete
            $("#btnQuizDelete_" + interactionCounter).click ->
                $("#dialog-confirm-Quiz").css("display","block")
                $('#dialog-confirm-Quiz').dialog
                  modal: true
                  buttons:
                    'Löschen': ->
                            $(this).dialog 'close'
                            $("#fgpNeu_Quiz_" + interactionCounter).toggle "drop", 200, () ->
                                $("#fgpNeu_Quiz_" + interactionCounter).remove()
                            return
                    'Abbrechen': ->
                            $(this).dialog 'close'
                            return

            # Dropdown Event for btnSetQuizOnTrueReferences
            $("#btnSetQuizOnTrueReferences_" + interactionCounter).click ->
                # für OnTrue
                oldValue = $("#btnSetQuizOnTrueReferences_" + interactionCounter).attr("currentEdge")
                if typeof oldValue != "undefined"
                    $("#btnSetQuizOnTrueReferences_" + interactionCounter).attr("oldValue", oldValue)
                createDropdownQuizOnTrue(interactionCounter, "#btnSetQuizOnTrueReferences_" + interactionCounter, "#" + stuff.id)

            # Dropdown Event for btnSetQuizOnFalseReferences
            $("#btnSetQuizOnFalseReferences_" + interactionCounter).click ->
                # für OnFalse
                oldValue = $("#btnSetQuizOnFalseReferences_" + interactionCounter).attr("currentEdge")
                if typeof oldValue != "undefined"
                    $("#btnSetQuizOnFalseReferences_" + interactionCounter).attr("oldValue", oldValue)
                createDropdownQuizOnFalse(interactionCounter, "#btnSetQuizOnFalseReferences_" + interactionCounter, "#" + stuff.id)

            btnEinklappen("#btnQuizEinklappen_" + interactionCounter, "#fstNeuesQuizContent_" + interactionCounter)
            # quizID Trigger
            $("#inQuizID_"+interactionCounter).keyup ->
                inIDSetter($("#inQuizID_"+interactionCounter), $("#lgdNeuesQuizFieldset_"+interactionCounter), "Quiz: ", "Neues Quiz")
             
            # inQuizStorypointRef Text setzen
            $("#inQuizStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
            # inItemStorypointRef Trigger
            $("#inStorypoint_" + counter).on 'input', () ->
                objectInput = $("#inQuizStorypointRef_" + interactionCounter)
                objectInput.val($("#inStorypoint_" + counter).val())
                if typeof objectInput.val() != "undefined"
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
            
            # Click Event für btnQuizAnswer
            $("#btnQuizAnswer_" + interactionCounter).click ->
                window.quizAnswerCounter++
                quizAnswerCounter = window.quizAnswerCounter
                copyAnswer = document.getElementById("fgpAnswer")
                answer = copyAnswer.cloneNode(true)
                answer.id = answer.id + "_" + quizAnswerCounter
                answer.style.display="block"
                document.getElementById("fstNeuesQuizContent_" + interactionCounter).appendChild(answer)
                setIDs($("#" + answer.id), quizAnswerCounter)

                # Click event für ddnstate
                createQuizDropdown( quizAnswerCounter ) 

                # Click Event für btnQuizAnswerDelete
                $("#btnQuizAnswerDelete_" + quizAnswerCounter).click ->
                    $("#dialog-confirm-Antwort").css("display","block")
                    $('#dialog-confirm-Antwort').dialog
                      modal: true
                      buttons:
                        'Löschen': ->
                                $(this).dialog 'close'
                                $("#" + answer.id).toggle "drop", 200, () ->
                                    $("#" + answer.id).remove()
                                return
                        'Abbrechen': ->
                                $(this).dialog 'close'
                                return
                helpRek( $("#" + answer.id))
            helpRek( $("#" + stuff.id))

        createQuizDropdown = (counter) ->
            #DropdownMenu QuizStatus
            $("#ddnTrue_" + counter).click ->
                $("#ddnState_" + counter).val("Wahr")
                $("#ddnState_" + counter).html("Wahr <span class='caret' />")
            $("#ddnFalse_" + counter).click ->
                $("#ddnState_" + counter).val("Falsch")
                $("#ddnState_" + counter).html("Falsch <span class='caret' />")


        createChooser = (counter) ->
                copyForm = document.getElementById("fgpNeu_" + $("#ddnInteractions_" + counter).val())
                window.interactioncounter++
                interactionCounter = window.interactioncounter
                stuff = copyForm.cloneNode(true)
                stuff.id = stuff.id + "_" + interactionCounter
                stuff.style.display="block"
                document.getElementById("fstNeuesStorypointContent_"+counter).appendChild(stuff)
                setIDs($("#" + stuff.id), interactionCounter)
                btnSwitchDown("#btnSwitchDown_" + interactionCounter, "#" + stuff.id)
                btnSwitchUp("#btnSwitchUp_" + interactionCounter, "#" + stuff.id)

                # Click Event für btnChooserDelete
                $("#btnChooserDelete_" + interactionCounter).click ->
                    $("#dialog-confirm-Chooser").css("display","block")
                    $('#dialog-confirm-Chooser').dialog
                      modal: true
                      buttons:
                        'Löschen': ->
                                $(this).dialog 'close'
                                $("#fgpNeu_Chooser_" + interactionCounter).toggle "drop", 200, () ->
                                    currentObj = $("#btnChooserAnswer_" + interactionCounter).parent().next()
                                    while typeof currentObj != 'undefined'
                                        id = currentObj.attr("id").split("_")
                                        oldValue = $("#btnSetChooserStorypointReferences_" + id[1]).attr("oldValue")
                                        if typeof oldValue != 'undefined'
                                            oldValue = oldValue.split("_")
                                            storypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_"+id[1], "fhlNeuerStorypoint" )
                                            storypoint = storypoint.split("_")
                                            RemoveParticularEdge(storypoint[1], oldValue[1])
                                        currentObj = currentObj.next()
                                    $("#fgpNeu_Chooser_" + interactionCounter).remove()
                                return
                        'Abbrechen': ->
                                $(this).dialog 'close'
                                return
                # Click Event für btnChooserEinklappen
                btnEinklappen("#btnChooserEinklappen_" + interactionCounter, "#fstNeuerChooserContent_" + interactionCounter)

                # ChooserName Trigger
                $("#inChooserID_"+interactionCounter).keyup ->
                   inIDSetter($("#inChooserID_"+interactionCounter), $("#lgdNeuerChooserFieldset_"+interactionCounter), "Chooser: ", "Neuer Chooser")

                # inChooserStorypointRef Text setzen
                $("#inChooserStorypointRef_" + interactionCounter).val($("#inStorypoint_" + counter).val())   
                # inItemStorypointRef Trigger
                $("#inStorypoint_" + counter).on 'input', () ->
                    objectInput = $("#inChooserStorypointRef_" + interactionCounter)
                    objectInput.val($("#inStorypoint_" + counter).val())
                    if typeof objectInput.val() != "undefined"
                        objectInput.val(objectInput.val().replace(/\s+/, "") )

                # Click Event für btnChooserAnswer
                $("#btnChooserAnswer_" + interactionCounter).click ->
                    window.chooserAnswerCounter++
                    ChooserAnswerCounter = window.chooserAnswerCounter
                    copyAnswer = document.getElementById("fgpChooserAnswer")
                    answer = copyAnswer.cloneNode(true)
                    answer.id = answer.id + "_" + ChooserAnswerCounter
                    answer.style.display="block"
                    document.getElementById("fstNeuerChooserContent_" + interactionCounter).appendChild(answer)
                    setIDs($("#" + answer.id), ChooserAnswerCounter)


                    # Dropdown Event for btnSetChooserItemReferences
                    $("#btnSetChooserItemReferences_" + ChooserAnswerCounter).click ->
                        createChooserItemReference(ChooserAnswerCounter, "#btnSetChooserItemReferences_" + ChooserAnswerCounter, "#" + stuff.id)
                     

                    # Dropdown Event for btnSetChooserStorypointReferences
                    $("#btnSetChooserStorypointReferences_" + ChooserAnswerCounter).click ->
                        oldValue = $("#btnSetChooserStorypointReferences_" + interactionCounter).attr("currentEdge")
                        if typeof oldValue != "undefined"
                            $("#btnSetChooserStorypointReferences_" + interactionCounter).attr("oldValue", oldValue)
                        createChooserStorypointReference(ChooserAnswerCounter, "#btnSetChooserStorypointReferences_" + ChooserAnswerCounter, "#" + stuff.id)
                        
   

                    # Click Event für btnChooserAnswerDelete
                    $("#btnChooserAnswerDelete_" + ChooserAnswerCounter).click ->
                        $("#dialog-confirm-Antwort").css("display","block")
                        $('#dialog-confirm-Antwort').dialog
                          modal: true
                          buttons:
                            'Löschen': ->
                                    $(this).dialog 'close'
                                    $("#" + answer.id).toggle "drop", 200, () ->
                                        oldValue = $("#btnSetChooserStorypointReferences_" + ChooserAnswerCounter).attr("oldValue")
                                        if typeof oldValue != 'undefined'
                                            oldValue = oldValue.split("_")
                                            storypoint = edgeStorypointfinder("#btnSetChooserStorypointReferences_"+ChooserAnswerCounter, "fhlNeuerStorypoint" )
                                            storypoint = storypoint.split("_")
                                            RemoveParticularEdge(storypoint[1], oldValue[1])
                                        $("#" + answer.id).remove()
                                    return
                            'Abbrechen': ->
                                    $(this).dialog 'close'
                                    return
                    helpRek( $("#" + answer.id))
                helpRek( $("#" + stuff.id))


