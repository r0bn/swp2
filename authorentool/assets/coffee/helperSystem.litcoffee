        initHelpSystem = () ->
           
            $("#divHelpBox").slideUp("slow")
            url = 'HelpContent.xml'
            $.ajax
                type: 'GET'
                url: 'HelpContent.xml'
                dataType: 'xml'
                success: (xml) ->
                    $(xml).find("Content").children().each () ->
                        $("#divHelpBox").attr(this.tagName.toLowerCase(), $(this).text())
                    helpRek($("#rowFormular"))   
                    return
                     
            return

        helpRekRemoveID = (node) ->
            clearedID = node.split("_")
            return clearedID[0]

        helpRek = (node) ->
            node.children().each ->
                helpID = $(this).attr("id")
                if typeof helpID != 'undefined'
                    $("#"+helpID).on "focus", () ->
                        $("#divHelpBox").empty()
                        $("#divHelpBox").append($("#divHelpBox").attr(helpRekRemoveID(helpID).toLowerCase()))
                        return
                helpRek($(this))
            return

