<html>
<head>
<title>test page for story creation</title>
</head>
<body>

{!! Form::open(array('url'=>'createstory','method'=>'POST', 'files'=>true)) !!}
<h2>story metadata:</h2>

{!! Form::label('title', 'title') !!}
<br/>
{!! Form::text('title') !!}
<br/><br/>
{!! Form::label('description')!!}
<br/>
{!! Form::text('description') !!}
<br/><br/>
{!! Form::label('author')!!}
<br/>
{!! Form::text('author') !!}
<br/><br/>
{!! Form::label('size')!!}
<br/>
{!! Form::text('size') !!}
<br/><br/>
{!! Form::label('creation_date')!!}
<br/>
{!! Form::text('creation_date') !!}
<br/><br/>
{!! Form::label('location')!!}
<br/>
{!! Form::text('location') !!}
<br/><br/>
{!! Form::label('radius')!!}
<br/>
{!! Form::text('radius') !!}

<h2>xml file:</h2>
{!! Form::text('xml_file') !!}
<br/>
<h2>media files (multiple selection possible):</h2>
{!! Form::file('media[]', array('multiple'=>true)) !!}
<br/><br/><br/>
{!! Form::submit('upload!') !!}
{!! Form::close() !!}

</body>
</html>