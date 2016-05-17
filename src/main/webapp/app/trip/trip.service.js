'use strict';

(() => {
  angular
    .module('airline.trip')
    .service('tripService', TripService)

  TripService.$inject = ['accessService', '$http']

  function TripService (accessService, $http) {
    this.getLocations = function () {
      return $http
        .get('./api/tickets/locations')
        .then(response => response.data)
    }

    this.getTripsById = function (id) {
      return $http
        .get('./api/tickets/trips/' + id)
        .then(response => response.data)
    }
  }
})()
