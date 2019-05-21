using System;

namespace BookingPlaceAPI.Helpers
{
    public class PlacementGenerator
    {
        private readonly static Random random = new Random();

        public static int GetRandomCountOfAvailableOlacement() => random.Next(8, 20);
    }
}
