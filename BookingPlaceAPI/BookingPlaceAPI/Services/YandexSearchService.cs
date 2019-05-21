using BookingPlaceAPI.Models;
using Newtonsoft.Json;
using System;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Services
{
    public class YandexSearchService : IYandexSearchService
    {
        public async Task<YandexCompanyMetaData> SearchOrganization(string organizationName)
        {
            try
            {
                var client = new HttpClient();
                var response = await client.GetAsync(new Uri(@"https://search-maps.yandex.ru/v1/?text=Тула%20кафе&&type=biz&lang=ru_RU&results=1000&apikey=8b5186af-488c-4725-b1f4-34621747bfda"));
                if (!response.IsSuccessStatusCode)
                    return null;

                var responseBody = await response.Content.ReadAsStringAsync();
                var serachResult = JsonConvert.DeserializeObject<YandexOrganizationInfo>(responseBody);
                return serachResult?.Features?.FirstOrDefault(el => el.Properties?.Name == organizationName)?.Properties?.CompanyMetaData;
            }
            catch(Exception ex)
            {
                return null;
            }
        }
    }
}
