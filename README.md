# TaxiBooking_DB
Sample Taxi Booking Application

Requirement:

A call taxi operator has a feet of 'n' cars. [For simplicity take count as 5, but the program should work for any number of taxis. You can also name them as Taxi-1, Taxi-2... Taxi-n]. Each customer is uniquely identified as CUST-1, CUST-2...

There are five pick-up / drop points in the city. The points are DLF, Velachery, Tambaram, T Nagar and Nungambakkam
All the points are on a straight line and the distance between the points are the same - 15 kms. [i.e. DLF to Velachery = 15 kms, Velachery to Tambaram = 15 kms and so on]

Time taken to travel between each point is 15 mins [i.e. 1 km / min]
Taxi charges are Rs. 50 for the first 5 kms, and then Rs. 10 for every kilo meter thereafter

During booking, the customer provides - Starting point, ending point and booking time
After dropping a customer, the taxi waits at the same point for next customer to be allotted.

The booking algorithm is - find a taxi which is free and nearest to the customer location and allot it. If more than one taxi is present at the same/nearest location, preference is given to the taxi which has earned the least during the day

If a taxi has to travel to a different location for pickup, it looses money at the rate of Rs. 5 per KM. So, an empty taxi between DLF and Velachery incurs a loss of Rs. 75

Assume all taxis are at DLF initially.
Once a taxi is allotted, wait for next set of inputs
Also, the program should be able to display travel history of a given taxi.
