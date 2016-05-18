'use strict';

(() => {
  angular
    .module('airline.trip')
    .config(config)

  config.$inject = ['tripRoutes', '$stateProvider']

  function config (tripRoutes, $stateProvider) {
    Object.keys(tripRoutes)
      .forEach(key => {
        $stateProvider
          .state(key, tripRoutes[key])
      })
  }
})()
