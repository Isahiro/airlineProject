'use strict';

(() => {
  angular
    .module('airline.trip')
    .constant('tripRoutes', {
      reviewTrips: {
        url: '/trips/{username}',
        templateUrl: 'app/trip/trip-review.template.html',
        controller: 'TripReviewController',
        controllerAs: 'tripReview',
        data: {
          loggedIn: true
        }
      },

      planTrip: {
        url: '/trips/planner',
        templateUrl: 'app/trip/trip-planner.template.html',
        controller: 'TripPlannerController',
        controllerAs: 'tripPlanner',
        data: {
          loggedIn: true
        }
      }
    })
})()
