<?php namespace App\Providers;

use Illuminate\Support\ServiceProvider;

class StorytellarServiceProvider extends ServiceProvider
{
    /*
    * Because we use the Repository Pattern this is the necessary binding between
    * Interface and Repository
    */
    public function register()
    {
        $this->app->bind(
            'App\Interfaces\AuthHelper',
            'App\Repositories\AuthHelper'
        );

        $this->app->bind(
            'App\Interfaces\FileHelper',
            'App\Repositories\FileHelper'
        );

        $this->app->bind(
            'App\Interfaces\Story',
            'App\Repositories\Story'
        );

        $this->app->bind(
            'App\Interfaces\TempStory',
            'App\Repositories\TempStory'
        );

        $this->app->bind(
            'App\Interfaces\XmlParser',
            'App\Repositories\XmlParser'
        );

        $this->app->bind(
            'App\Interfaces\XmlValidator',
            'App\Repositories\XmlValidator'
        );
    }
}