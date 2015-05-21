        btnSwitchDown = (buttonID, currentObjID) ->
            $(buttonID).click ->
                nextObject = $(currentObjID).next()
                currentObject = $(currentObjID)
                if typeof nextObject.attr("id") == 'undefined' || nextObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        nextObject.insertBefore("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnSwitchUp = (buttonID, currentObjID) ->
            $(buttonID).click ->
                prevObject = $(currentObjID).prev()
                currentObject = $(currentObjID)
                if typeof prevObject.attr("id") == 'undefined' || prevObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        prevObject.insertAfter("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnEinklappen = (button, content) ->
            # Click Event für btnQuizEinklappen
            $(button).click ->
                if $(content).is( ":hidden" )
                    $(content).show( "slow" )
                    $(button).find("span").addClass('glyphicon-resize-small')
                    $(button).find("span").removeClass('glyphicon-resize-full')
                    return
                else
                    $(content).slideUp("slow")
                    $(button).find("span").removeClass('glyphicon-resize-small')
                    $(button).find("span").addClass('glyphicon-resize-full')
                    return
            return

        setIDs = (node, counter) ->
            node.children().each () ->
                if typeof $(this).attr("id") != "undefined"
                    newID =  $(this).attr("id") + "_" + counter
                    $(this).attr("id", newID)
                    if typeof $(this).attr("for") != "undefined"
                        newFor = $(this).attr("for") + "_" + counter
                        $(this).attr("for", newFor)
                setIDs($(this), counter)
            return
