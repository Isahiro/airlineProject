'use strict';

(() => {
  angular
  .module('airline')
  .config(config)

  config.$inject = [
    'airlineRoutes',
    '$stateProvider',
    '$urlRouterProvider',
    '$locationProvider',
    '$mdThemingProvider'
  ]

  function config (
    airlineRoutes,
    $stateProvider,
    $urlRouterProvider,
    $locationProvider,
    $mdThemingProvider
  ) {
    Object.keys(airlineRoutes)
      .forEach(key => {
        $stateProvider
          .state(key, airlineRoutes[key])
      })

    $urlRouterProvider.otherwise('/')
    $locationProvider.html5Mode(true)

    $mdThemingProvider.theme('default')
  }
})()
