namespace BookingPlaceAPI.Models
{
    public class UserRating
    {
        public int UserId { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public double Rate { get; set; }
        public string Text { get; set; }
        public string Phone { get; set; }
    }
}
