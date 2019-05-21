using System;
using System.Text;

namespace BookingPlaceAPI.Helpers
{
    public class PhoneGenerator
    {
        private readonly static Random random = new Random();

        public static string GetRandomPhone()
        {
            var result = new StringBuilder().Append("+79");
            for (int index = 0; index < 9; index++)
                result.Append(random.Next(0, 10));

            return result.ToString();
        }
    }
}
