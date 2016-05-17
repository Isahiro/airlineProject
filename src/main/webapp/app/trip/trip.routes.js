'use strict';

(() => {
  angular
    .module('fastbook.trip')
    .constant('tripRoutes', {
      reviewTrips: {
        url: '/trips/{username}',
        templateUrl: 'app/trip/trip-review.template.html',
        controller: 'TripReviewController',
        controllerAs: 'tripReview',
        resolve: {
          trips: ['tripService', '$stateParams', function (tripService, $stateParams) {
            tripService.getTripsById($stateParams.id)
          }]
        },
        data: {
          loggedIn: true
        }
      },

      planTrip: {
        url: '/trips/planning',
        templateUrl: 'app/trip/trip-planning.template.html',
        controller: 'TripPlanningController',
        controllerAs: 'tripPlanner',
        resolve: {
          locations: ['tripService', function (tripService) {
            tripService.getLocations()
          }]
        },
        data: {
          loggedIn: true
        }
      }
    })
})()
