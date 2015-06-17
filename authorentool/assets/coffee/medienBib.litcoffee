        initialLightMedienBox = () ->
            lightMedienBox()
            lightMedienBox2()

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
              
        lightMedienBox2 = () ->
            $medien = $('#medienLightbox2')
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
              
        selectFromMedienBib1 = () ->
            $('#medienLightbox').modal("show")
            $('#medienLightbox .btn-select').click (handler) ->
                $(window.mbibCallerField).val($(this).data("filename"))
                $('#medienLightbox').modal("hide")
                return
            return
        selectFromMedienBib2 = () ->
            $('#medienLightbox2').modal("show")
            $('#medienLightbox2 .btn-select').click (handler) ->
               $(window.mbibCallerField).val($(this).data("filename"))
               $('#medienLightbox2').modal("hide")
               return
            return
