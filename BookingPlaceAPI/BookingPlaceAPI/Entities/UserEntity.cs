namespace BookingPlaceAPI.Entities
{
    public class ShortUserInfo
    {
        public int Id { get; set; }
        public string Phone { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
    }

    public class UserEntity : ShortUserInfo
    {
        public string Password { get; set; }
    }
}
