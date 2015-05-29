
        checkSafeButton = () ->
            if window.safeButtonCounter > 0            
                $("#btnSaveStory").css("display","block")
            else
                $("#btnSaveStory").css("display","none")
            return


        initScrollbar = () ->
            $header = $('#colColumn2')
            headerPos = $header.position().top

            $win = $(window)
            $win.scroll ->
                #Hier scrollt man nach unten. Dabei gilt: immer 30px unter Top und fix
              if $win.scrollTop() >= headerPos
                #alert "nach unten" + $win.scrollTop()
                $header.css
                  'position': 'fixed'
                  'top': '30px'
                  'right': '20px'
                #Hier wird für jede Position wenn man nach oben Scrollt berechnet, wie die 
                #das Div sich anzupassen hat.
              i = 0
              while i <= headerPos
                if $win.scrollTop() == i
                    $header.css
                        'position': 'fixed'
                        'top': Math.abs(i-headerPos)
                        'right':'20px'
                i++
              return


   
            
        initDropdownClicks = () ->
            #DropdownMenu Radius
            $("#ddnme").click -> 

                if $("#inRadius").val() != "" && $("#ddnradius").val() == "Kilometer"
                    $("#inRadius").val(Math.ceil($("#inRadius").val() * 1000))
                $("#ddnradius").val("Meter")
                $("#ddnradius").html("Meter <span class='caret' />")
            $("#ddnkm").click -> 
                if $("#inRadius").val() != "" && $("#ddnradius").val() == "Meter"
                    $("#inRadius").val(Math.ceil($("#inRadius").val() / 1000))
                $("#ddnradius").val("Kilometer")
                $("#ddnradius").html("Kilometer <span class='caret' />")

            #DropdownMenu Size
            $("#ddnkb").click -> 
                $("#ddnsize").val("KB")
                $("#ddnsize").html("KB <span class='caret' />")
            $("#ddnmb").click -> 
                $("#ddnsize").val("MB")
                $("#ddnsize").html("MB <span class='caret' />")
            $("#ddngb").click -> 
                $("#ddnsize").val("KB")
                $("#ddnsize").html("GB <span class='caret' />")


        inIDSetter = (objectInput, objectFieldset, text, alternativeText) ->
                    objectInput.val(objectInput.val().replace(/\s+/, "") )
                    if objectInput.val() != ""
                        text = text + objectInput.val()
                        objectFieldset.text(text)
                    else
                        objectFieldset.text(alternativeText)

        initDdnInteraction = (counter) ->
                $("#ddnChooser_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Chooser")
                    $("#ddnInteractions_" + counter).html("Chooser <span class='caret'/>")
                $("#ddnQuiz_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Quiz")
                    $("#ddnInteractions_" + counter).html("Quiz <span class='caret'/>")
                $("#ddnItem_" + counter).click ->
                    $("#ddnInteractions_" + counter).val("Item")
                    $("#ddnInteractions_" + counter).html("Item <span class='caret'/>")



