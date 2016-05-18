'use strict';

(() => {
  angular
    .module('airline.trip')
    .controller('TripPlannerController', TripPlannerController)

  TripPlannerController.$inject = [
    'locations', 'tripService', 'accessService', '$scope', '$state']

  function TripPlannerController (
    locations, tripService, accessService, $scope, $state
  ) {
    this.locations = locations
    this.origin = tripService.getOrigin
    this.destination = tripService.getDestination
    this.errorMessage = undefined
    this.routes

    this.findRoutes = () => {
      tripService
        .findRoutes(this.origin, this.destination)
        .then(routes => this.routes = routes)
        .then(() => $scope.apply)
    }

    this.bookRoute = (route) => {
      this.errorMessage = undefined

      tripService
        .bookRoute(this.origin.id, this.destination.id, route)
        .then(trip => {
          if (trip !== undefined) {
            this.origin = undefined
            this.destination = undefined
            this.routes = undefined
            $state.go('reviewTrips')
          } else {
            this.errorMessage = 'There was a problem booking this route. Please try another route'
            this.findRoutes()
          }
        })
    }
  }
})()
