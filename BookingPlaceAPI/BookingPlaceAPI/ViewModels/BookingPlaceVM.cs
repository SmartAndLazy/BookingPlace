namespace BookingPlaceAPI.ViewModels
{
    public class BookingPlaceVM
    {
        public int? OrganizationId { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Phone { get; set; }
        public string Date { get; set; }
        public string FromTime { get; set; }
        public int? NumberOfTables { get; set; }
    }
}
