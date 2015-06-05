        lightMedienBox = () ->
            $medien = $('#medienLightbox')
            $('[data-target="#medienLightbox"]').on 'click', (event) ->
              $medienBib = $(this).find('medienBib')
              css = 
                'maxWidth': $(window).width() - 100
                'maxHeight': $(window).height() - 100
              $medien.find('.close').addClass 'hidden'
              $medienBib.css css
              return
            $medien.on 'shown.bs.modal', (e) ->
              $medienBib = $(this).find('medienBib')
              $medien.find('.modal-dialog').css 'width': $medienBib.width()
              $medien.find('.close').removeClass 'hidden'
              return

        selectFromMedienBib1 = (targetId) ->
            $('#medienLightbox').modal("show")

            $('#medienLightbox .btn-select').click (handler) ->
                $("##{targetId}").val($(this).data("filename"))
                $('#medienLightbox').modal("hide")

        selectFromMedienBib2 = (targetId) ->
            $('#medienLightbox2').modal("show")

            $('#medienLightbox2 .btn-select').click (handler) ->
                $("##{targetId}").val($(this).data("filename"))
                $('#medienLightbox2').modal("hide")

