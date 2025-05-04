
## Timeline

**Date:** 4 May  
**Time Taken:** 1 hour 51 minutes  

---

## Rough Work


Building
Floor
Parking Spot
Spot Type

Vehicle

interface FindSpotStrategy {}

iterface SpotVehicleManagement {}

class SpotService {
    void addSpot();
    void deleteSpot();
    void getSpots();
    void getSpot(int id);

}

class VehicleService {
    void getVehicles();
    void getVehicles();
}

class SpotVehicleManagementService {
    vehicle_to_spot
    spot_to_vehicle
}

ParkingLotSystem {
    SpotService
    VehicleService
    defaultFindSpotStrategy

    list_available_spots
    park_vehicle
    unpark_vehicle
}

Floor
- ParkingSpots

Building
- Floors
- ParkingLot