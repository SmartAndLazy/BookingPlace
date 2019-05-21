using System;

namespace BookingPlaceAPI.Helpers
{
    public class OrganizationHelper
    {
        private readonly static Random random = new Random();

        public static int GetCountEmptyPlacement(int maxValue) => random.Next(0, maxValue);
    }
}
