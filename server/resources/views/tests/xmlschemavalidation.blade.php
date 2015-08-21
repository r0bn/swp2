<!DOCTYPE html>
<html>
<head>
    <meta name="robots" content="noindex,nofollow">
    <title>XML VALIDATION TEST</title>
    <link rel="stylesheet" href="/css/app2.css"/>
</head>
<body>

<div class="swp2-container swp2-container-center">

    <div class="swp2-width-1-1 swp2-panel swp2-panel-box swp2-panel-box-primary swp2-margin-top swp2-margin-bottom">
        <h3 class="swp2-panel-title">XML VALIDATION TEST</h3>
        This input will be tested for:
        <ul class="swp2-list">
            <li>Well formed</li>
            <li>Schema valid</li>
            <li>Metadata valid</li>
        </ul>
    </div>


    {!! Form::open(array('action'=>'StorytellarController@postXmlSchemaValidation','method'=>'POST', 'class' =>
    'swp2-form')) !!}


    <textarea id="xml-validation-input" name="xmlstring" rows="20" class="swp2-width-1-1"></textarea>


    <button type="text" id="xml-validation-btn"
            class="swp2-button swp2-button-success swp2-button-large swp2-align-right swp2-margin-top">
        Test
    </button>

    {!! Form::close() !!}

</div>

<div class="swp2-container swp2-container-center swp2-margin-top">
    <div id="result-valid" class="swp2-alert swp2-alert-success">Result: Valid!</div>
    <div id="result-invalid" class="swp2-alert swp2-alert-danger">Result: Not valid!</div>
</div>

<script src="/js/jquery.js"></script>
<script src="/js/app2.js"></script>
<script>
    $(document).ready(function () {
        $("#result-valid").hide();
        $("#result-invalid").hide();
        $.ajaxSettings.traditional = true;
        $("#xml-validation-btn").click(function (e) {
            e.preventDefault();
            $("#result-valid").hide();
            $("#result-invalid").hide();

            var data = $("#xml-validation-input").serialize();

            $.ajax({
                url: '/docs/tests/xmlschemavalidation',
                type: 'POST',
                data: data,

                success: function (response) {
                    if (response) {
                        $("#result-valid").fadeIn();
                    } else {
                        $("#result-invalid").fadeIn();
                    }
                },
                error: function() {
                    $("#result-invalid").fadeIn();
                }
            });

        });
    });
</script>
</body>
</html>