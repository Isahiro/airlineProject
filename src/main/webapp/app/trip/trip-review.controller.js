'use strict';

(() => {
  angular
    .module('airline.trip')
    .controller('TripReviewController', TripReviewController)

  TripReviewController.$inject = [
    'reviewTrips', 'tripService', '$state', '$scope']

  function TripReviewController (
    reviewTrips, tripService, $state, $scope
  ) {
    this.trips = reviewTrips

    this.cancelTrip = (trip) => {
      tripService
        .cancelTrip(trip)
        .then(trips => {
          this.trips = trips
          $scope.apply()
        })
    }

    this.updateTrip = (origin, destination) => {
      tripService.setOrigin(origin)
      tripService.setDestination(destination)
      $state.go('planTrip')
    }
  }
})()
