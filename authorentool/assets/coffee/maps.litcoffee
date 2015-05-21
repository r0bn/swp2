        addMarker = (location, markers) ->
            center = new google.maps.LatLng(location.A, location.F)
            marker = new google.maps.Marker({
                position: location,
                map: window.map
            });
            marker.set("markerOwner", window.mapCaller)
            markers.push(marker)
            if window.mapCaller == "btnMap" && $("#ddnradius").val() != "Einheit"
                rad = $("#inRadius").val()
                if $("#ddnradius").val() == "Einheit"
                    rad = 0
                else if $("#ddnradius").val() == "Kilometer"
                    rad = rad * 1000
                circle = new (google.maps.Circle)
                    center: center
                    map: window.map
                    radius: Math.ceil(rad)
                    strokeColor: 'red'
                    strokeOpacity: 0.8
                    strokeWeight: 2
                    fillColor: 'blue'
                    fillOpacity: 0.35
                    editable: true
                circle.set("markerOwner", window.mapCaller)
                markers.push(circle)

                google.maps.event.addListener circle, 'radius_changed', () ->
                                    rad = circle.getRadius()
                                    if $("#ddnradius").val() == "Meter"
                                        $("#inRadius").val(Math.ceil(rad))
                                    else if $("#ddnradius").val() == "Kilometer"
                                        $("#inRadius").val(Math.ceil(rad / 1000))
                google.maps.event.addListener circle, 'center_changed', () ->
                    marker.setPosition(circle.center)
                google.maps.event.addListener marker, 'position_changed', () ->
                    lat = marker.position.A
                    lng = marker.position.F
                    $("#inLatLngLocation").val(lat + ", " + lng)
                return
            return

        setAllMap = (map, markers) ->
          i = 0
          while i < markers.length
            markers[i].setMap map
            i++
          return

      

        googleMap = () ->

                mapOptions = 
                  zoom: 8
                  center: new (google.maps.LatLng)(48.7758459, 9.182932100000016)
                  scaleControl: true
                window.map = new (google.maps.Map)($('#gmeg_map_canvas')[0], mapOptions)
                input = document.getElementById('inMapSearch')
                window.map.controls[google.maps.ControlPosition.TOP_LEFT].push input
                searchBox = new (google.maps.places.SearchBox)(input)
                autocomplete = new google.maps.places.Autocomplete(input, { types: ['geocode'] })
                autocomplete.bindTo('bounds', window.map);
                markers = []
                google.maps.event.addListener searchBox, 'places_changed', ->
                  `var marker`
                  `var i`
                  places = searchBox.getPlaces()
                  if places.length == 0
                    return
                  i = 0
                  marker = undefined
                  if typeof markers != 'undefined'
                    while marker = markers[i]
                        marker.setMap null
                        i++
                  # For each place, get the icon, place name, and location.
                  markers = []
                  bounds = new (google.maps.LatLngBounds)
                  i = 0
                  place = undefined
                  while place = places[i]
                    image = 
                      url: place.icon
                      size: new (google.maps.Size)(71, 71)
                      origin: new (google.maps.Point)(0, 0)
                      anchor: new (google.maps.Point)(17, 34)
                      scaledSize: new (google.maps.Size)(25, 25)
                    # Create a marker for each place.
                    setAllMap(null, markers)
                    markers = []
                    marker = new (google.maps.Marker)(
                      map: window.map
                      icon: image
                      title: place.name
                      position: place.geometry.location)
                    markers.push marker


                    $("#inLatLngLocation").val(place.geometry.location.A + ", " + place.geometry.location.F)
                    bounds.extend place.geometry.location
                    i++
                  window.map.fitBounds bounds
                  return

                $(window).resize ->

                  google.maps.event.trigger window.map, 'resize'

                  return
                google.maps.event.addListener window.map, 'click', (event) ->
                    lat = event.latLng.lat()
                    lng = event.latLng.lng()
                    currentGPSField = $("#" + window.mapCaller).attr("gpsField")
                    $("#" + currentGPSField).val(lat + ", " + lng)
                    i = 0
                    max = markers.length
                    deleted = []
                    while i < max
                        markers[i].setMap null
                        if markers[i].get("markerOwner") == window.mapCaller
                            deleted.push(i)
                        i++
                    i = deleted.length
                    i--
                    while i > -1
                        markers.splice(deleted[i], 1);
                        i--
                    addMarker(event.latLng, markers);
                    return
                lightBox(markers)

        lightBox = (markers) ->
                $lightbox = $('#lightbox')
                $('[data-target="#lightbox"]').on 'click', (event) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  css = 
                    'maxWidth': $(window).width() - 100
                    'maxHeight': $(window).height() - 100
                  $lightbox.find('.close').addClass 'hidden'
                  $mapDiv.css css
                  google.maps.event.trigger window.map, 'resize'
                  return
                $lightbox.on 'shown.bs.modal', (e) ->
                  $mapDiv = $(this).find('gmeg_map_canvas')
                  $lightbox.find('.modal-dialog').css 'width': $mapDiv.width()
                  $lightbox.find('.close').removeClass 'hidden'
                  i = 0
                  while i < markers.length
                       if markers[i].get("markerOwner") == window.mapCaller
                            markers[i].setMap window.map
                            markers[i].getMap().setCenter(markers[i].position);
                            if markers[i] instanceof google.maps.Circle
                                rad = $("#inRadius").val()
                                if $("#ddnradius").val() == "Einheit"
                                    rad = 0
                                else if $("#ddnradius").val() == "Kilometer"
                                    rad = rad * 1000
                                markers[i].set('radius', Math.ceil(rad))
                       i++
                  google.maps.event.trigger window.map, 'resize'
                  return
                $lightbox.on 'hide.bs.modal', (e) ->
                    setAllMap(null, markers)





