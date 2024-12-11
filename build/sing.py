import requests
import re

def download_latest_prerelease_sing_box_amd64():
  """
  Downloads the latest pre-release Sing-box for Linux amd64.
  """
  repo_owner = "SagerNet"
  repo_name = "sing-box"
  api_url = f"https://api.github.com/repos/SagerNet/sing-box/releases"

  try:
    response = requests.get(api_url)
    response.raise_for_status()  # Raise an exception for bad status codes

    releases = response.json()
    
    # Find the latest pre-release
    latest_prerelease = None
    for release in releases:
      if release["prerelease"]:
        latest_prerelease = release
        break

    if latest_prerelease:
      # Find the amd64 asset
      for asset in latest_prerelease["assets"]:
        if "linux-amd64.tar.gz" in asset["name"]:
          download_url = asset["browser_download_url"]
          
          # Download the file using requests
          print(f"Downloading from: {download_url}")
          with requests.get(download_url, stream=True) as r:
              r.raise_for_status()
              with open(asset["name"], 'wb') as f:
                  for chunk in r.iter_content(chunk_size=8192):
                      f.write(chunk)
          print(f"Downloaded: {asset['name']}")
          return
      print("No amd64 pre-release asset found.")
    else:
      print("No pre-release found.")

  except requests.exceptions.RequestException as e:
    print(f"Error: {e}")

if __name__ == "__main__":
  download_latest_prerelease_sing_box_amd64()