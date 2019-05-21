using System;

namespace BookingPlaceAPI.Helpers
{
    public class TimeGenerator
    {
        private readonly static Random random = new Random();

        public static int GetRandomStartHour() => GetRandomValue(new[] { 7, 8, 9, 10 });
        public static int GetRandomEndHour() => GetRandomValue(new[] { 19, 20, 21, 22, 23, 24 });

        private static int GetRandomValue(int[] collection) => collection[random.Next(0, collection.Length)];
    }
}
