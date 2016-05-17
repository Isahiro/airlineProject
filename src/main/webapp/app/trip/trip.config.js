'use strict';

(() => {
  angular
    .module('airline.trip')
    .config(config)

  config.$inject = ['tripRoutes', '$stateProvider']

  function config (tripRoutes, $stateProvider) {
    Object.keys(tripRoutes) // JS built in function
      .forEach(key => {
        $stateProvider
          .state(key, tripRoutes[key])  // accesses each state object given the key and the object
      })
  }
})()
