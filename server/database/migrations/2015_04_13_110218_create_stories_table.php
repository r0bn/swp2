<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateStoriesTable extends Migration {

	/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
		Schema::create('stories', function(Blueprint $table)
		{
			$table->increments('id');
            $table->string('title');
            $table->string('description');
            $table->string('author');
            $table->string('size');
            $table->string('creation_date');
            $table->string('location');
            $table->string('radius');
            $table->string('media');
            $table->binary('xml_file');
			$table->timestamps();
		});
	}

	/**
	 * Reverse the migrations.
	 *
	 * @return void
	 */
	public function down()
	{
		Schema::drop('stories');
	}

}
